function  compare() {
    var s1 =document.getElementById("string1").value;
    var s2 = document.getElementById("string2").value;
    if(s1!=null&&s1!=""&&s2!=null&&s2!=null){
        var content={
            "s1":s1,
            "s2":s2,
        }
        $.ajax({
            url:"/simCompare.do",
            data : content,
            contentType:'application/x-www-form-urlencoded;charset=utf-8',
            success:function (data) {
                document.getElementById("result").innerHTML="";
                var table = document.createElement("table");
                var row = table.insertRow();
                var vectitle = row.insertCell();
                var sdptitle = row.insertCell();
                vectitle.innerHTML="Word2Vec";
                sdptitle.innerHTML="SDP";
                var reRow = table.insertRow();
                var vecRe = reRow.insertCell();
                var sdpRe = reRow.insertCell();
                vecRe.innerHTML=data["vec"];
                sdpRe.innerHTML=data["sdp"];
                document.getElementById("result").appendChild(table);
            }
        })
    }

}