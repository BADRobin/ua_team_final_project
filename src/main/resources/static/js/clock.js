var week = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];

function updateTime() {
    var now = new Date();

    document.getElementById("time").innerText =
        zeroPadding(now.getHours(), 2) + ':' +
        zeroPadding(now.getMinutes(), 2);
    // + ':' +
    //     zeroPadding(now.getSeconds(), 2);
    document.getElementById("date").innerText =
        zeroPadding(now.getDate(), 2) + '-' +
        zeroPadding(now.getMonth()+1, 2) + '-' +
        zeroPadding(now.getFullYear(), 4) + ' ' +


        week[now.getDay()];
};
var timerID = setInterval(updateTime, 1000);
updateTime();
function zeroPadding(num, digit) {
    var zero = '';
    for(var i = 0; i < digit; i++) {
        zero += '0';
    }
    return (zero + num).slice(-digit);
}