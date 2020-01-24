

function wordInit() {
    const param = getRequest();
    const num = param["num"];
    const type = param["type"];
    if(num == null || num==="" || type == null || type === ""){
        alert("param is invalid!");
        window.location.href = "./index.html";
        return;
    }
    if(type === "collect")$("#collect").remove();//如果是收藏的单词，就删除收藏按钮

    $.ajax({
        url: "./word/getWords",
        type: "GET",
        data: {"type":type,"num":num},
        success: function (data) {
            word_list = data;
            for(let i=0;i<data.length; ++i){//注册音频
                if(data[i].voiceAddress == null)continue;
                createjs.Sound.registerSound({src:data[i].voiceAddress, id:i});
            }
            let dom = $(".pagination");
            dom.empty();
            dom.append('<li style="display:none"><a href="javascript:void(0)">hidden</a></li>');
            for(let i=0; i<data.length; ++i){
                dom.append('<li><a href="javascript:void(0)" onclick="changeWord('+ i + ')">' + (i+1) + '</a></li>');
                if(i%10 === 9)dom.append('<br/>');
            }
            dom.append('<li style="display:none"><a href="javascript:void(0)">hidden</a></li>');
            changeWord(0);
        },
        error: function (data) {
            myAlert("fail","fetch the words fail! "+JSON.stringify(data));
        }
    });
}

//下一个word
function nextWord() {
    if(now_word_index+1 >= word_list.length){
        myAlert("Error","already the last!");
    }
    else changeWord(now_word_index+1);
}

//上一个word
function preWord() {
    if(now_word_index <=0 ){
        myAlert("Error","already the first!");
    }
    else changeWord(now_word_index-1);
}

// 改变当前显示的word
function changeWord(index) {
    now_word_index = index;
    const word = word_list[index];
    $("#wordPicture").attr("src",word.pictureAddress);
    $("#wordExample").text(word.wordExample);
    $("#exampleTranslate").text(word.exampleTranslate);
    $("#wordName").text(word.wordName);
    $("#translate").text(word.wordTranslate);
    let list = $(".pagination").children();
    const text=String(now_word_index+1);
    for(let dom of list){
        if(dom.textContent === text)dom.className="active";
        else dom.className="";
    }
}