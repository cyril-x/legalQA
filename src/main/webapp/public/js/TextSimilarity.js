function shortsimil() {
    var Stext1 = document.getElementById("shotextS1").value;
    var Stext2 = document.getElementById("shotextS2").value;

    if(Stext1==""||Stext2==""){
        alert("输入文本为空")
    }
    else {
        var data={};
            data.text1=Stext1,
            data.text2=Stext2,

        $.ajax({
            url:"/shorttext",
            //dataType:"json",
            data:data,
            type:"GET",
            success:function(data){

              // alert(data);
                document.getElementById("STSresult").innerHTML=data;

            },
            error:function () {
                alert("NetWork Error");
            }
        })
    }
}
function longsimil() {
    var Stext1 = document.getElementById("longtextS1").value;
    var Stext2 = document.getElementById("longtextS2").value;

    if(Stext1==""||Stext2==""){
        alert("输入文本为空")
    }
    else {
        var data={
            "text1":Stext1,
            "text2":Stext2
        };
        $.ajax({
            url:"longtext",
            //dataType:"json",
            data:data,
            type:"GET",
            success:function(data){
                //alert(data);
                document.getElementById("LTSresult").innerHTML=data;

            },
            error:function () {
                alert("NetWork Error");
            }
        })
    }
}