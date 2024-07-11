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

function getBasicBusinessHours(basicHours){
	let sun = convertingDate(basicHours, "sun");
	let mon = convertingDate(basicHours, "mon");
	let tue = convertingDate(basicHours, "tue");
	let wed = convertingDate(basicHours, "wed");
	let thu = convertingDate(basicHours, "thu");
	let fri = convertingDate(basicHours, "fri");
	let sat = convertingDate(basicHours, "sat");
	let hol = convertingDate(basicHours, "hol");
	return [sun, mon, tue, wed, thu, fri, sat, hol];
}

function showBusinessHour(hoursArr){
    let week = ["일", "월", "화", "수", "목", "금", "토", "공휴일"];
    //0:일, 1:월, 2:화 .... 6:토
    for(let i = 0; i < hoursArr.length; i++){
        let day = hoursArr[i];
        let startTime = day.day[0];
        let endTime = day.day[1];
        let lunchStart = day.day[2];
        let lunchEnd = day.day[3];
        
        let listItem = document.createElement("div");
        listItem.innerHTML = week[i] + " - 영업시간 : <span class='workHour'>" + startTime + " ~ " + endTime
                             +"</span> , 점심시간 : <span class='lunchHour'>" + lunchStart + " ~ " + lunchEnd +"</span>";

        if(startTime === 0){
            listItem.querySelector(".workHour").innerHTML = "휴무";
        }
        if(lunchStart === 0){
            listItem.querySelector(".lunchHour").innerHTML = "-";
        }
        
        document.querySelector("#working_hour").appendChild(listItem);
    }
}