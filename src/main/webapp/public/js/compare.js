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
                var reI = table.insertRow();
                var reIAll = reI.insertCell();
                var reIcheck = reI.insertCell();
                var reII = table.insertRow();
                var reIIAll = reII.insertCell();
                var reIIcheck = reII.insertCell();
                vecRe.innerHTML=data["vec"];
                sdpRe.innerHTML=data["sdp"];
                for(var i=0;i<data["queryI"].length;i++){
                    reIcheck.innerHTML+=data["queryI"][i]+"<br>";
                }
                for(var i=0;i<data["queryII"].length;i++){
                    reIIcheck.innerHTML+=data["queryII"][i]+"<br>";
                }

                for(var i=0;i<data["queryIAll"].length;i++){
                   reIAll.innerHTML+=data["queryIAll"][i]+"<br>";
                }
                for(var i=0;i<data["queryIIAll"].length;i++){
                    reIIAll.innerHTML+=data["queryIIAll"][i]+"<br>";
                }
                document.getElementById("result").appendChild(table);
            }
        })
    }

}