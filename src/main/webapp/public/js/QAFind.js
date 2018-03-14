function findAnswer() {
    var inputdata = document.getElementById("queuetext").value;
    var dmif = $("#dm_classify").prop("checked");
    if (inputdata!=null&&inputdata!="") {
        var conten = {
            "input": inputdata,
            "dm":dmif,
        }

        $.ajax({
            url: "/queandan.do",
            data: conten,
            type: "GET",
            success: function (data) {
                document.getElementById("answer").innerHTML = "";

                var responsetable = document.createElement("table");
                var responsetrtitle = responsetable.insertRow();
                var questionnumtitle = responsetrtitle.insertCell();
                var dmtitle = responsetrtitle.insertCell();
                var questiontitletitle = responsetrtitle.insertCell();
                var questionContenttitle = responsetrtitle.insertCell();
                var bestresponsetitle = responsetrtitle.insertCell();
                var similaritytitle = responsetrtitle.insertCell();

                questionnumtitle.innerHTML = "num";
                dmtitle.innerHTML = "dm";
                questiontitletitle.innerHTML = "Title";
                questionContenttitle.innerHTML = "Content";
                bestresponsetitle.innerHTML = "answer";
                similaritytitle.innerHTML = "similarity";
                if(data[0]["re"]==0){
                    alert("未找到答案");

                }else {

                for (var m =1 ; m < data[0]["re"]; m++) {
                    var responsetr = responsetable.insertRow();
                    var questionnum = responsetr.insertCell();
                    var dm = responsetr.insertCell();
                    var questiontitle = responsetr.insertCell();
                    var questionContent = responsetr.insertCell();
                    var bestresponse = responsetr.insertCell();
                    var similarity = responsetr.insertCell();
                    questionnum.innerHTML = data[m]["num"];
                    dm.innerHTML = data[m]["dm"];
                    questiontitle.innerHTML = data[m]["questionTitle"];
                    questionContent.innerHTML = data[m]["questionContent"];
                    bestresponse.innerHTML = data[m]["response"];
                    similarity.innerHTML = data[m]["result"];
                }
                document.getElementById("answer").appendChild(responsetable);}
            },
            error: function () {
                alert("NetWork Error");
            }
        })
    }else {
        alert("请输入问题")
    }
}