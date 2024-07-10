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
let feedTickets = 0
let playTickets = 0
let experience = 0
let level = 1
let maxExperience = 100
let feedText
let playText
let experienceText
let userId = localStorage.getItem("MemberId"); // 예시를 위한 사용자 ID

function preload() {
  // 필요한 에셋 로드
  this.load.image('background', '/images/background.png')
  this.load.spritesheet('pet_level_1', '/images/pet.png', { frameWidth: 230, frameHeight: 180 })
  this.load.image('feedButton', '/images/feedButton.png')
  this.load.image('playButton', '/images/playButton.png')
}

function create() {
  this.add.image(190, 300, 'background')

  this.anims.create({
    key: 'idle',
    frames: this.anims.generateFrameNumbers('pet_level_1', { frames: [0, 1, 2, 3, 4, 5] }),
    frameRate: 10,
    repeat: -1
  })

  pet = this.add.sprite(200, 300, 'pet_level_1')
  pet.play('idle')
  pet.setScale(0.5)
  pet.x = 180
  pet.y = 400

  feedButton = this.add.image(60, 40, 'feedButton').setInteractive()
  feedButton.on('pointerdown', () => {
    if (feedTickets > 0) {
      feedTickets--
      experience += 10
      if (experience >= maxExperience) {
        level++
        experience = 0
        maxExperience += 50
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
      updateText()
      saveData()
    }
  })
  playButton.setScale(0.5)

  feedText = this.add.text(55, 65, `${feedTickets}`, { fontSize: '20px', fill: '#fff' })
  playText = this.add.text(125, 65, `${playTickets}`, { fontSize: '20px', fill: '#fff' })
  experienceText = this.add.text(190, 50, `Experience: ${experience}`, { fontSize: '20px', fill: '#fff' })

  loadData()
}

function update() {
  // 업데이트 로직 (필요한 경우)
}

function updateText() {
  feedText.setText(`${feedTickets}`)
  playText.setText(`${playTickets}`)
  experienceText.setText(`Experience: ${experience}`)
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
      updateText()
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
    maxExperience: maxExperience
  }

  fetch(`http://localhost:9001/api/petgame/${userId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    },
    body: JSON.stringify(data)
  })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error))
}
