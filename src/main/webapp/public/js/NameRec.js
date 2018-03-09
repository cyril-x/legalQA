function nameRec() {
    var inputdata = document.getElementById("nameinput").value;
    if (inputdata!=null){
        var conten ={
            "input":inputdata,
        }
        $.ajax({
            url:"/namerec",
            data:conten,
            type:"POST",
            success:function(data){
                document.getElementById("NameRecresultl").innerHTML="";
                document.getElementById("sentence").innerHTML="";
                var jsondata = $.parseJSON($.parseJSON(data)["success"]);
                var len = jsondata.length;
                for (var j=0;j<len;j++){
                    var sennode = document.createElement("h3");
                    var sentextnode = document.createTextNode(jsondata[j]['sentence']);
                    sennode.appendChild(sentextnode);
                    document.getElementById("sentence").appendChild(sennode);
                    var sen = jsondata[j]["sentence"];
                    //document.getElementById("NameRecresult").innerHTML=sen;
                    var nodes = jsondata[j]['nodes'];
                    for (var i=1;i<nodes.length;i++){
                        var node = document.createElement("li");
                        var textnode = document.createTextNode(nodes[i]["expression"]);
                        node.appendChild(textnode);
                        document.getElementById("NameRecresultl").appendChild(node);
                    }
                }
            },
            error:function () {
                alert("NetWork Error");
            }
        })

        $.ajax({
          url:"/ansjrec",
          data:conten,
          type:"POST",
          success:function (data) {
              document.getElementById("NameRecresultr").innerHTML="";
              var jsondata = $.parseJSON(data)["success"];
              var len = jsondata.length;
              for (var m =0;m<len;m++){
                  var node = document.createElement("li");
                  var textnode = document.createTextNode(jsondata[m]["name"]);
                  node.appendChild(textnode);
                  document.getElementById("NameRecresultr").appendChild(node);
              }


          },
            error:function () {
                alert("NetWork Error");
            }

        })



    }else{
        alert("文本为空");
    }

}