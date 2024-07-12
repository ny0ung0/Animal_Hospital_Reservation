 let today = new Date();
 let selectedDate;
 //0:일, 1:월, 2:화 .... 6:토
 let selectedDay;
 let selectedVet;
 let selectedTime;
 let selectedSlot = null; 
 const date = document.querySelector("input[type=date]");
 const vet = document.querySelector("#vet");
 const timeSlot = document.querySelector("#time_slot");



function setDateLimit(){
	//오늘 날짜보다 이전 날짜는 선택할 수 없도록  
	date.setAttribute("min", today.toISOString().substring(0,10))
	//max 날짜는 3개월 오늘 날짜 전일
	today.setMonth(today.getMonth()+2)
	date.setAttribute("max", today.toISOString().substring(0,10))
}
setDateLimit();


function loadBasicInfo(data){
	let userInfo = data[0];
	let vetAvailInfo = data[1];
	let vetInfo = data[2];
	let vetNamesNIds = Object.keys(vetAvailInfo);
	let basicHours = JSON.parse(vetInfo[Object.keys(vetInfo)[0]].businessHours);
	let basicHoursArr = getBasicBusinessHours(basicHours);
	
console.log(data)
console.log(userInfo)
	  //병원 이름 넣기
	  document.querySelector("#vetName").setAttribute("value", Object.keys(vetInfo)[0]);
	  //로그인한 사용자 이름 넣어주기
	  document.querySelector("input[name=user_name]").value = userInfo.user.name;
	  //포인트정보넣기
	  document.querySelector("#point").innerHTML = userInfo.pointList[0] ? userInfo.pointList[0] : "0";
	  if(!vetInfo[Object.keys(vetInfo)[0]].partnership == true){
		  document.querySelector(".point_container").style.display="none";
	  }
	  
	  
	  //쿠폰 정보 넣기
	  for(let i = 0; i < userInfo.couponList.length; i++){
		  let coupon = userInfo.couponList[i];
		  let listItem = document.createElement("option");
		  listItem.setAttribute("value", coupon.id);
		  listItem.innerHTML = coupon.name + ', 발행날짜' + formattingDate(coupon.issueDate) + ', 만료날짜' + formattingDate(coupon.expiryDate);
		  document.querySelector("#coupon").appendChild(listItem);
	  }
	  //반려동물정보넣기
	  for(let i = 0; i < userInfo.petList.length; i++){
		  let pet = userInfo.petList[i];
		  let listItem = document.createElement("option");
		  listItem.setAttribute("value", pet.id);
		  listItem.innerHTML = pet.name + '';
		  document.querySelector("#pet").appendChild(listItem);
	  }
	  //수의사정보넣기
	  for(let i = 0; i < vetNamesNIds.length; i++){
		  console.log(vetNamesNIds[i])
		  let vet = vetNamesNIds[i].split("//")
		  let vetName = vet[1];
		  let vetId = vet[0];
		
		  let listItem = document.createElement("option");
		  listItem.setAttribute("value", vetId);
		  listItem.innerHTML = vetName + ' 수의사 선생님';
		  document.querySelector("#vet").appendChild(listItem);
	  }
	  
	//시간 뿌려주기(의사, 날짜 를 선택하는 순간 그에 맞춰서 시간 바꿔주기)
	// //기본적으로 영업시간에 기준해서 시간 뿌려주기

	  vet.addEventListener("change", function(e){
		  document.querySelector("#time_slot").innerHTML="";
			selectedVet = e.target.value
//	 		console.log(selectedVet)
//	 		console.log(selectedDate)
			if(selectedDate == null){
				alert("진료 예약을 원하는 날짜를 먼저 선택해주세요!")
			}
			
			loadTimeslot(basicHoursArr,vetAvailInfo);

		})
}

function getBasicBusinessHours(basicHours){
	let sun = convertingDate(basicHours, "sun");
	let mon = convertingDate(basicHours, "mon");
	let tue = convertingDate(basicHours, "tue");
	let wed = convertingDate(basicHours, "wed");
	let thu = convertingDate(basicHours, "thu");
	let fri = convertingDate(basicHours, "fri");
	let sat = convertingDate(basicHours, "sat");
	let hol = convertingDate(basicHours, "hol");
	return [sun, mon, tue, wed, thu, fri, sat];
}

function convertToTimeZone(date, timeZone) {
	
    // 시간대를 변환한 날짜를 생성
    const dateInTimeZone = new Date(date.toLocaleString('en-US', { timeZone }));

    // 년, 월, 일을 가져옴
    const year = dateInTimeZone.getFullYear();
    const month = String(dateInTimeZone.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더함
    const day = String(dateInTimeZone.getDate()).padStart(2, '0');

    // yyyy-MM-dd 형식으로 반환
    return year+"-"+ month+"-"+day;
}


function formattingDate(date){
	let newDate = date.slice(0,10);
	return newDate;
}

function convertingDate(basicHours, day){
	let startTime;
	let endingTime;
	let lunchStart;
	let lunchEnd;
	//영업하는 날 시간 구하기
	if(basicHours[day][0].slice(7) !="영업 안함"){
		startTime = basicHours[day][0].slice(7).split("//")[0];
		endingTime = basicHours[day][0].slice(7).split("//")[1];
		//영업하면서&점심시간있음
		if(basicHours[day][1].slice(7) !="점심시간 없음"){
			lunchStart = basicHours[day][1].slice(7).split("//")[0];
			lunchEnd = basicHours["mon"][1].slice(7).split("//")[1];
		}else{
			//영업하지만&점심시간없음
			lunchStart = 0;
			lunchEnd = 0;
		}
	}else{
		//영업 안 하는 날
		startTime = 0;
		endingTime = 0;
		lunchStart = 0;
		lunchEnd = 0;
	}
	return {day : [startTime, endingTime, lunchStart, lunchEnd]};
}

function showDates(startTime, endTime, lunchStart, lunchEnd){
	if(startTime == 0|| endTime == 0){
		document.querySelector("#time_slot").innerHTML="<div class='msg'>해당일은 예약가능한 시간이 없습니다😥</div>"
		return;
	}
    // 시간을 분 단위로 변환
    let [startHour, startMinute] = startTime.split(":").map(Number);
    let [endHour, endMinute] = endTime.split(":").map(Number);
    let [lunchStartHour, lunchStartMinute] = lunchStart != 0 ? lunchStart.split(":").map(Number) : [0,0];
    let [lunchEndHour, lunchEndMinute] = lunchEnd != 0 ? lunchEnd.split(":").map(Number) : [0,0];
    let startTotalMinutes = startHour * 60 + startMinute;
    let endTotalMinutes = endHour * 60 + endMinute;
    let lunchStartTotalMinutes = lunchStartHour * 60 + lunchStartMinute;
    let lunchEndTotalMinutes = lunchEndHour * 60 + lunchEndMinute;
    
    // 30분 간격으로 버튼 생성
    let currentMinutes = startTotalMinutes;
    while (currentMinutes < endTotalMinutes) {
        let hours = Math.floor(currentMinutes / 60);
        let minutes = currentMinutes % 60;
        
        let timeString = String(hours).padStart(2, '0') + ":" + String(minutes).padStart(2, '0');
        let listItem = document.createElement("span");
        listItem.setAttribute("value", timeString);
        listItem.classList = "time_slot"
        listItem.innerHTML = timeString;
        document.querySelector("#time_slot").appendChild(listItem);
        currentMinutes += 30;
        
        if (currentMinutes > lunchStartTotalMinutes && currentMinutes <= lunchEndTotalMinutes) {
            listItem.classList.add("disabled");
        } else {
            listItem.addEventListener("click", function() {
                // click 이벤트 핸들링
                console.log("예약 시간: " + timeString);
            });
        }
        listItem.addEventListener("click", function(e) {
        	//click이벤트 핸들링
            console.log("예약 시간: " + timeString);
        	selectedTime = timeString;
        	if (selectedSlot) {
                selectedSlot.style.backgroundColor = ""; 
            }
            e.target.style.backgroundColor = "#4C5CB3";
            e.target.style.color = "white";
            
            selectedSlot = e.target;
        });
    }
}

function loadTimeslot(basicHoursArr,vetAvailInfo){
	//해당날짜 기본 타임슬롯보여주기
	if(basicHoursArr[selectedDay] !=null){
		let selectedBasicTime = basicHoursArr[selectedDay]["day"]
		showDates(selectedBasicTime[0], selectedBasicTime[1], selectedBasicTime[2], selectedBasicTime[3])
		//해당날짜 해당선생님의 availability보여주기
		Object.keys(vetAvailInfo).forEach(key=>{
			if(key.split("//")[0] == selectedVet){
				for(v of vetAvailInfo[key]){
					if(convertToTimeZone(v.date, 'Asia/Seoul') == selectedDate){
						console.log(v.time.slice(0,5))
						document.querySelector("span[value='"+v.time.slice(0,5)+"']").classList.add("disabled");
					}
				}
			}
		})
	}
}


date.addEventListener("change", function(e){
	if(selectedVet != null){
		selectedVet="";
		vet.value= "default";
		document.querySelector("#time_slot").innerHTML="";
	}
	selectedDate = e.target.value
	selectedDay = new Date(selectedDate).getDay();
})