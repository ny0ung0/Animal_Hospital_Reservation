const config = {
  type: Phaser.AUTO,
  width: 380,
  height: 600,
  parent: 'game-container',
  scene: {
    preload: preload,
    create: create,
    update: update
  }
}

const game = new Phaser.Game(config)

let pet
let feedButton
let playButton
let feedTickets
let playTickets
let experience
let level
let maxExperience
let feedText
let playText
let experienceText
let levelText
let lastGivenTimeMorning
let lastGivenTimeAfternoon
let lastGivenTimeEvening
let isFullyGrown
let userId = localStorage.getItem("MemberId")

function preload() {
  this.load.image('background', '/images/background.png')
  this.load.spritesheet('pet_level_1', '/images/dog_sprite.png', { frameWidth: 320, frameHeight: 240 })
  this.load.image('feedButton', '/images/feedButton.png')
  this.load.image('playButton', '/images/playButton.png')
}

function create() {
  this.add.image(190, 300, 'background')

  this.anims.create({
    key: 'idle',
    frames: this.anims.generateFrameNumbers('pet_level_1', { frames: [0, 1, 2, 3] }),
    frameRate: 6,
    repeat: -1
  })

  pet = this.add.sprite(200, 300, 'pet_level_1')
  pet.play('idle')
  pet.setScale(0.5)
  pet.x = 180
  pet.y = 420

  feedButton = this.add.image(60, 40, 'feedButton').setInteractive()
  feedButton.on('pointerdown', () => {
    if (feedTickets > 0) {
      feedTickets--
      experience += 10
      if (experience >= maxExperience) {
        if (level >= 3) {
        } else {
          experience = 0
          maxExperience += 50
          level++
        }
        if (level >= 3 && experience >= 200) {
          isFullyGrown = true
        }
      }
      updateText()
      saveData()
    }
  })
  feedButton.setScale(0.5)

  playButton = this.add.image(130, 35, 'playButton').setInteractive()
  playButton.on('pointerdown', () => {
    if (playTickets > 0) {
      playTickets--
      experience += 10
      if (experience >= maxExperience) {
        level++
        experience = 0
        maxExperience += 50
      }
      if (level >= 3 && experience >= 200) {
        isFullyGrown = true
      }
      updateText()
      saveData()
    }
  })
  playButton.setScale(0.5)

  feedText = this.add.text(55, 65, `${feedTickets}`, { fontSize: '20px', fill: '#fff' })
  playText = this.add.text(125, 65, `${playTickets}`, { fontSize: '20px', fill: '#fff' })
  experienceText = this.add.text(190, 50, `Experience: ${experience}`, { fontSize: '20px', fill: '#fff' })
  levelText = this.add.text(190, 25, `Level: ${level}`, { fontSize: '20px', fill: '#fff' })

  loadData()
}

function update() {
  // 업데이트 로직 (필요한 경우)
}

function updateText() {
  feedText.setText(`${feedTickets}`)
  playText.setText(`${playTickets}`)
  experienceText.setText(`Experience: ${experience}`)
  levelText.setText(`Level: ${level}`)
}

function loadData() {
  fetch(`http://localhost:9001/api/petgame/${userId}`)
    .then(response => response.json())
    .then(data => {
      feedTickets = data.feedCount
      playTickets = data.playCount
      experience = data.currentExperience
      level = data.level
      maxExperience = data.maxExperience
      lastGivenTimeMorning = data.lastGivenTimeMorning
      lastGivenTimeAfternoon = data.lastGivenTimeAfternoon
      lastGivenTimeEvening = data.lastGivenTimeEvening
      isFullyGrown = data.isFullyGrown
      updateText()
      checkAndGiveTickets() // 데이터를 로드한 후에 티켓 지급 확인
    })
    .catch(error => console.error('Error:', error))
}

function saveData() {
  const data = {
    Id: userId,
    feedCount: feedTickets,
    playCount: playTickets,
    currentExperience: experience,
    level: level,
    maxExperience: maxExperience,
    lastGivenTimeMorning: lastGivenTimeMorning,
    lastGivenTimeAfternoon: lastGivenTimeAfternoon,
    lastGivenTimeEvening: lastGivenTimeEvening,
    isFullyGrown: isFullyGrown
  }

  fetch(`http://localhost:9001/api/petgame/${userId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    },
    body: JSON.stringify(data)
  })
    .then(response => response.json())
    .then(data => {
      if (isFullyGrown) {
        loadData() // 새로운 펫 데이터를 로드
      } else {
        console.log(data)
      }
    })
    .catch(error => console.error('Error:', error))
}

function checkAndGiveTickets() {
  const currentHour = new Date().getHours()
  const now = new Date()

  if (currentHour >= 7 && currentHour < 9) {
    if (lastGivenTimeMorning) {
      const lastGivenDate = new Date(lastGivenTimeMorning)
      if (lastGivenDate.getDate() === now.getDate()) {
        return // 이미 현재 시간대에 티켓을 지급받았으므로 더 이상 처리하지 않음
      }
    }
    feedTickets++
    lastGivenTimeMorning = now.toISOString() // 마지막 지급 시간 업데이트
  } else if (currentHour >= 12 && currentHour < 15) {
    if (lastGivenTimeAfternoon) {
      const lastGivenDate = new Date(lastGivenTimeAfternoon)
      if (lastGivenDate.getDate() === now.getDate()) {
        return // 이미 현재 시간대에 티켓을 지급받았으므로 더 이상 처리하지 않음
      }
    }
    feedTickets++
    lastGivenTimeAfternoon = now.toISOString() // 마지막 지급 시간 업데이트
  } else if (currentHour >= 18 && currentHour < 21) {
    if (lastGivenTimeEvening) {
      const lastGivenDate = new Date(lastGivenTimeEvening)
      if (lastGivenDate.getDate() === now.getDate()) {
        return // 이미 현재 시간대에 티켓을 지급받았으므로 더 이상 처리하지 않음
      }
    }
    feedTickets++
    lastGivenTimeEvening = now.toISOString() // 마지막 지급 시간 업데이트
  }
  updateText()
  saveData()
}
