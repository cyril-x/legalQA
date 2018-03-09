function findAnswer() {
    var inputdata = document.getElementById("queuetext").value;
    if (inputdata!=null) {
        var conten = {
            "input": inputdata,
        }
    }
    $.ajax({
        url:"/queandan.do",
        data:conten,
        type:"GET",
        success:function (data) {
            document.getElementById("answer").innerHTML="";

            var responsetable = document.createElement("table");
            var responsetrtitle = responsetable.insertRow();
            var questionnumtitle = responsetrtitle.insertCell();
            var questionContenttitle = responsetrtitle.insertCell();
            var bestresponsetitle = responsetrtitle.insertCell();
            var similaritytitle = responsetrtitle.insertCell();
            questionnumtitle.innerHTML = "num";
            questionContenttitle.innerHTML = "Content";
            bestresponsetitle.innerHTML = "answer";
            similaritytitle.innerHTML = "similarity";

            for (var m =0;m<3;m++){
                var responsetr = responsetable.insertRow();
                var questionnum = responsetr.insertCell();
                var questionContent = responsetr.insertCell();
                var bestresponse = responsetr.insertCell();
                var similarity = responsetr.insertCell();
                questionnum.innerHTML = data[m]["num"];
                questionContent.innerHTML = data[m]["questionContent"];
                bestresponse.innerHTML = data[m]["response"];
                similarity.innerHTML = data[m]["result"];
                /*var node = document.createElement("li");
                var textnode = document.createTextNode(data[m]["num"]);
                node.appendChild(textnode);
                document.getElementById("answer").appendChild(node);
                var node = document.createElement("li");
                var textnode = document.createTextNode(data[m]["questionContent"]);
                node.appendChild(textnode);
                document.getElementById("answer").appendChild(node);
                var node = document.createElement("li");
                var textnode = document.createTextNode(data[m]["response"]);
                node.appendChild(textnode);
                document.getElementById("answer").appendChild(node);
                var node = document.createElement("li");
                var textnode = document.createTextNode(data[m]["result"]);
                node.appendChild(textnode);
                document.getElementById("answer").appendChild(node);*/
            }
            document.getElementById("answer").appendChild(responsetable);
        },
        error:function () {
            alert("NetWork Error");
        }
    })
}