
function wordInit() {
    const param = getRequest();
    const num = param["num"];
    const type = param["type"];
    if(num == null || num==="" || type == null || type === ""){
        alert("param is invalid!");
        window.location.href = "./index.html";
        return;
    }

    $.ajax({
        url: "./word/getWords",
        type: "GET",
        data: {"type":type,"num":num},
        success: function (data) {
            word_list = data;
            if(word_list === undefined || word_list.length === 0) {
                // 如果查询到的单词库为空，就返回上一页
                toastr.error("no word was query !! ");
                setTimeout(function () {// 3s 后刷新
                    window.history.back();
                }, 1000);
                return;
            }

            // 初始化每个单词的 是否收藏，是否记住 两个属性
            if(type === "new"){
                for(let word of word_list){
                    word.is_collected = false;
                    word.is_recited = false;
                }
            }
            else if(type=== "collect") {
                for(let word of word_list){
                    word.is_collected = true;
                    judgeRecited(word);
                }
            }
            else{//review
                for(let word of word_list){
                    word.is_recited = true;
                    judgeCollected(word);
                }
            }

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
            toastr.error("fetch the words fail! "+JSON.stringify(data),"fail!");
        }
    });
}

//下一个word
function nextWord() {
    if(now_word_index+1 >= word_list.length){
        toastr.warning("already the last!","warning");
    }
    else changeWord(now_word_index+1);
}

//上一个word
function preWord() {
    if(now_word_index <=0 ){
        toastr.warning("already the first!","warning");
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
    // 改变两个按钮
    changeReciteButton(word_list[index].is_recited);
    changeCollectButton(word_list[index].is_collected);
}


// 判断是否 已记住
function judgeRecited(word){
    $.ajax({
        url: "word/judgeRecited",
        type: "GET",
        data: {"wordName": word.wordName},
        success: function (data) {
            word.is_recited = (data === 1);
        },
        error: function () {
            word.is_recited = false;
        }
    });
}


// 判断是否 已收藏
function judgeCollected(word) {
    $.ajax({
        url: "word/judgeCollected",
        type: "GET",
        data: {"wordName":word.wordName},
        success: function (data) {
            word.is_collected = (data === 1);
        },
        error: function (data) {
            word.is_collected = false;
        }
    });
}

// 收藏单词
function collectWord() {
    let word = word_list[now_word_index];
    if(word === undefined)return;
    if(word.is_collected === true){
        toastr.info("you are already collect this word!");
        return;
    }
    $.ajax({
        url: "word/collectWord",
        type: "GET",
        data: {"wordName":word.wordName},
        success: function (data) {
            if(data===1){
                word.is_collected = true;
                changeCollectButton(true);
                toastr.success("collection!!","success");
            }
            else toastr.info("collect fail! "+JSON.stringify(data),"fail");
        },
        error: function (data) {
            toastr.info("submit fail! "+JSON.stringify(data),"fail");
        }
    });
}

// 记住单词
function reciteWord() {
    let word = word_list[now_word_index];
    if(word === undefined)return;
    if(word.is_recited === true){
        toastr.info("you are already recite this word!");
        return;
    }
    $.ajax({
        url:"word/reciteWord",
        type: "GET",
        data: {"wordName":word.wordName},
        success: function (data) {
            if(data===1){
                word.is_recited = true;
                changeReciteButton(true);
                toastr.success("recite!!","success");
            }
            else toastr.info("recite fail! "+JSON.stringify(data),"fail");
        },
        error: function (data) {
            toastr.info("submit fail! "+JSON.stringify(data),"fail");
        }
    });
}

// 改变当前收藏按钮， 是显示已收藏还是显示将要收藏
function changeCollectButton(arg) {
    if(arg)$("#collect").html('<i class="fa fa-check"></i>已收藏');
    else $("#collect").html('<i class="fa"></i>收藏');
}

// 改变当前的记住按钮，是显示已记住还是显示将要记住
function changeReciteButton(arg) {
    if(arg)$("#recite").html('<i class="fa fa-check"></i>已记住');
    else $("#recite").html('<i class="fa"></i>记住');
}