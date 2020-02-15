"use strict";

const article_page_size = 10; //常量，文章列表每一页的文章数
const max_show_page_num = 10; //最多显示的分页的个数

/**
 *   articleList.html 需要的函数
 */

// 得到文章分类的列表
function getCategory() {
    $.ajax({
        url: "article/category",
        method: "GET",
        success : function(data){
            insertCategory(data);
            now_categoryId = data[0].categoryId;
            getArticle(data[0].categoryId,1);//默认先展示第一个分类的第一页
        },
        error: function (data) {
            toastr.error("fetch the article category fail! "+JSON.stringify(data),"fail!");
        }
    });
}

// 得到某一分类的某个页的文章列表
function getArticle(categoryId,page) {
    page = (page-1)*article_page_size;//偏移量从0开始
    let payload= {"categoryId":categoryId,"start": page, "size":article_page_size ,"type":type };
    if(param["username"] !== undefined)payload.username = param["username"];

    $.ajax({
        url: "article/articleList",
        method: "GET",
        data: payload,
        // (start,start+size]
        success: function(data){
            insertArticle(data);
        },
        error: function (data) {
            toastr.error("fetch article list fail! "+JSON.stringify(data),"fail");
        }
    });
}

// 将文章的目录加载到网页
function insertCategory(category_list) {
    let category = $("#category");
    category.empty();
    for(let i=0; i<category_list.length; ++i){
        category.append("<li " + (i===0? "class='active'":"") +
            "><a href='javascript:void(0)' onclick='changeCategory("+category_list[i].categoryId+ ");'>" +
            category_list[i].name + "<span class='pull-right categoryId" + category_list[i].categoryId + "'></span></a></li>");
    }
    // 初始化分页
    now_page=1;
    max_page=1;
    // 获得每一个分类的文章数
    for(let i=0; i<category_list.length; ++i) {
        let payload = {"categoryId": category_list[i].categoryId,
                        "type": type };
        if(param["username"] !== undefined)
            payload.username = param["username"];
        $.ajax({
            url: "article/articleCount",
            type: "GET",
            data: payload,
            success: function (data) {
                if(i === 0){
                    max_page = Math.ceil(data / article_page_size);
                    pageInit(); //初始化底部分页栏
                }
                $(".categoryId"+category_list[i].categoryId).text("("+data+")");
            },
            error: function () {
                $(".categoryId"+category_list[i].categoryId).text("null");
            }
        });
    }
}

// 将文章列表加载到网页去
function insertArticle(article_list) {
    let article = $("#article");
    article.empty();
    if(article_list.length === 0){
        article.append('<h1 class="text-warning">No Article</h1>');
        return;
    }
    for(let i=0; i<article_list.length; ++i){
        article.append('<div class="col-md-12 col-sm-12" style="background-color: #f8ffff"><div class="single-blog two-column">' +
            '<div class="post-content overflow">' +
            '<h2 class="post-title bold"><a href="articleDetail.html?articleId='+article_list[i].articleId+'">'+ article_list[i].title +'</a></h2>' +
            '<h3 class="post-author">Posted by <a href="./userHome.html?username='+article_list[i].username+'" target="_blank">'+ article_list[i].username +'</h3>' +
            '<p style="margin: 20px">' + article_list[i].content.substring(0,100) + '[...]</p>' +
            '<a href="articleDetail.html?articleId='+article_list[i].articleId +'" class="read-more">View More</a>' +
            '<div class="post-bottom overflow">' +
            '<ul class="nav navbar-nav post-nav">' +
            '<li><a href="javascript:void(0)"><i class="fa fa-folder-open"></i>'+article_list[i].clicks+' Clicks</a></li>' +
            '<li><a href="javascript:void(0)"><i class="fa fa-thumbs-up"></i><span class="articleId'+article_list[i].articleId+'agree"></span></a></li>' +
            '<li><a href="javascript:void(0)"><i class="fa fa-heart"></i><span class="articleId'+article_list[i].articleId+'collect"></span></a></li>' +
            '<li><a href="javascript:void(0)"><i class="fa fa-comments"></i><span class="articleId'+article_list[i].articleId+'comment"></span></a></li>' +
            '</ul></div></div></div></div>');
    }
    for(let item of article_list){
        const payload = {"articleId":item.articleId};
        $.ajax({//collections
            url: "article/articleCollectCount",
            type: "GET",
            data: payload,
            success: function (num) {
                $(".articleId"+item.articleId+"collect").text(num+" Collects");
            },
            error: function () {
                $(".articleId"+item.articleId+"collect").text("null Collects");
            }
        });
        $.ajax({//agrees
            url: "article/articleAgreeCount",
            type: "GET",
            data: payload,
            success: function (num) {
                $(".articleId"+item.articleId+"agree").text(num+" Agrees");
            },
            error: function () {
                $(".articleId"+item.articleId+"agree").text("null Agrees");
            }
        });
        $.ajax({//comments
            url: "article/commentCount",
            type: "GET",
            data: payload,
            success: function (num) {
                $(".articleId"+item.articleId+"comment").text(num+" Comments");
            },
            error: function () {
                $(".articleId"+item.articleId+"comment").text("null Comments");
            }
        })
    }
}

//改变当前显示的目录
function changeCategory(categoryId) {
    now_categoryId = categoryId; //改变全局变量
    let list = $("#category").children();
    const substr = "changeCategory("+categoryId+")";
    for(let i=0; i<list.length; ++i){
        list[i].setAttribute("class","");
        if(list[i].innerHTML.indexOf(substr) >= 0){
            list[i].setAttribute("class","active");
            // 获取这个目录的文章数， 计算分页的个数
            let num=list[i].childNodes[0].childNodes[1].textContent;
            num = num.replace('(','');
            num = num.replace(')','');
            max_page = Math.ceil(num / article_page_size);
        }
    }
    now_page = 1;
    pageInit();
    getArticle(categoryId,1);
}

// 初始化 分页的栏
function pageInit() {
    let dom = $(".pagination");
    dom.empty();
    dom.append('<li><a href="javascript:void(0)" onclick="changePage(\'left\');">left</a></li>');//left
    for(let i=1;i <= max_page && i <= max_show_page_num; ++i){
        dom.append('<li ' + (i===now_page?'class="active"':'') + '><a href="javascript:void(0)" '+
            'onclick="changePage(' + i + ');">'+ i + '</a></li>');
    }
    dom.append('<li><a href="javascript:void(0)" onclick="changePage(\'right\')">right</a></li>');//right
}

// 改变页数
function changePage(page){
    let list = $(".pagination").children();
    if(page === "left" || page === "right"){
        let range_L = list[1].children[0].text;
        if(range_L === "right"){// 最大页数为 0 的时候
            toastr.error("no page!", "Error");
            return;
        }
        range_L = Number(range_L);
        let range_R = range_L + max_show_page_num; //确定当前的范围 [range_L,range_R)
        if(page === "left"){
            if(range_L === 1){
                toastr.info("No page change!");
                return;
            }
            for(let i=1; i+1<list.length;++i){
                list[i].children[0].text = range_L + i - 2;
                list[i].children[0].setAttribute("onclick",'changePage('+ (range_L + i -2) + ')');
                if(list[i].className === "active"){
                    if(i+1 === list.length-1) {//如果下一个元素是 right 了
                        now_page = now_page - 1;
                        getArticle(now_categoryId,now_page);
                    }
                    else { // 否则正常更新到下一个节点去
                        list[i].className = "";
                        list[i + 1].className = "active";
                    }
                }
            }
        }
        else{//right
            if(range_R > max_page){
                toastr.info("No page change!");
                return;
            }
            for(let i=1; i+1 < list.length; ++i){
                list[i].children[0].text = range_L + i;
                list[i].children[0].setAttribute("onclick",'changePage('+ (range_L + i) + ')');
                if(list[i].className === "active"){
                    if(i===1){//不能再向左推的时候
                        now_page = now_page + 1;
                        getArticle(now_categoryId,now_page);
                    }
                    else{
                        list[i].className="";
                        list[i-1].className="active";
                    }
                }
            }
        }
    }
    else{
        now_page = page;
        for(let dom of list){//把当前页变为active
            if(dom.children[0].text === String(page) )dom.className="active";
            else dom.className = "";
        }
        getArticle(now_categoryId,page);
    }
}


/**
 *  articleDetail.html 需要的函数
 */

//将请求的文章信息加载出来
function articleDetail() {
    const param = getRequest();
    let dom = $("#article_detail");
    dom.empty();
    if(param["articleId"] == null){
        dom.append('<h1 class="text-center text-danger">request is invalued!</h1>');
        return;
    }
    $.ajax({
        url: "article/getSingleArticle",
        type: "GET",
        data: {"articleId": param["articleId"]},
        success: function (data) {
            if(typeof (data) !== "object"){
                dom.append('<h1 class="text-center text-danger">fetch the article fail</h1>');
                toastr.error("fetch the article Error!" + data, "fail");
                return;
            }
            marked.setOptions({//设置markdown的语法高亮
                gfm:true,
                highlight: function (code,lang) {
                    return hljs.highlight(lang,code).value;
                }
            });
            $("title").text(data.title + " --文章阅读");

            dom.append('<h1 class="post-title bold" id="' + data.articleId + '">' + data.title+ '</h1><hr/>' +
            '<h5 class="post-author">' +
                '<span><i class="fa fa-user-plus"></i>Post By <a href="./userHome.html?username='+data.username+'" target="_blank">' + data.username+ '</a></span>' +
                '<span style="margin-left: 20px"><i class="fa fa-clock-o"></i>Create Time: ' + (new Date(data.createTime)).toLocaleString() + '</span>' +
                '<span style="margin-left: 20px"><i class="fa fa-clock-o"></i>Last Modified: '+ (new Date(data.modifyTime)).toLocaleString() + '</span>' +
            '</h5><hr/>' +
            '<div id="content" style="margin: 20px 10px;font-size: 18px">' + marked(data.content)+ '</div>' +
            '<div class="post-bottom overflow"><ul class="nav navbar-nav post-nav">' +
                '<li><a href="javascript:void(0)"><i class="fa fa-folder-open"></i>'+data.clicks+' Clicks</a></li>' +
                '<li><a href="javascript:void(0)" onclick="articleAgree('+ data.articleId+ ');"><i class="fa fa-thumbs-o-up"></i><span class="agree_num"></span></a></li>' +
                '<li><a href="javascript:void(0)" onclick="articleCollection('+ data.articleId + ');"><i class="fa fa-heart-o"></i><span class="collect_num"></span></a></li>' +
                '<li><a href="javascript:void(0)"><i class="fa fa-comments"></i><span class="comment_num"></span></a></li>' +
            '</ul></div>' + // end post-bottom
            '<div class="author-profile padding"><div class="row"><div class="col-sm-10 text-right">' +
                '<textarea class="form-control commentInput" rows="3" placeholder="我也评论一下!"></textarea>' +
                '<button class="btn btn-primary" style="margin: 15px" onclick="comment2article()">评论</button>' +
            '</div></div></div>' +
            '<div class="response-area"><h2 class="bold">Comments</h2><ul class="media-list" id="comment_list"></ul></div>');

            $.ajax({//获取收藏的数量
                url: "article/articleCollectCount",
                type: "GET",
                data: {"articleId":data.articleId},
                success: function (num) {
                    $(".collect_num").text(num+" Collects");
                },
                error: function () {
                    $(".collect_num").text("null Collects");
                }
            });
            $.ajax({//获取文章是否被收藏
                url: "article/getArticleCollect",
                type: "GET",
                data: {"articleId":data.articleId},
                success: function (data) {
                    if(data === 1){
                        let dom = $(".fa-heart-o");
                        dom.addClass("fa-heart");
                        dom.removeClass("fa-heart-o");
                    }
                }
            });
            $.ajax({//获取点赞的数量
                url: "article/articleAgreeCount",
                type: "GET",
                data: {"articleId":data.articleId},
                success: function (num) {
                    $(".agree_num").text(num+" Agrees");
                },
                error: function () {
                    $(".agree_num").text("null Agrees");
                }
            });
            $.ajax({//获取文章是否被点赞
                url: "article/getArticleAgree",
                type: "GET",
                data: {"articleId":data.articleId},
                success: function (data) {
                    if(data === 1){
                        let dom = $(".fa-thumbs-o-up");
                        dom.addClass("fa-thumbs-up");
                        dom.removeClass("fa-thumbs-o-up");
                    }
                }
            });
            addEditButton(data.articleId,data.username);// 页面上增加编辑按钮
            getComments(data.articleId);//然后加载对应的评论
        },
        error: function (data) {
            dom.append('<h1 class="text-center text-danger">fetch the article fail</h1>');
            toastr.error("fetch the article fail! "+JSON.stringify(data),"error");
        }
    });
}

// 如果文章阅读的用户和发表的是同一个用户，就会出现编辑按钮
function addEditButton(articleId,username) {
    let user={};
    getUser(user,function () {
        if(username === user.username){
            $(".edit").html('<button class="btn btn-lg btn-primary" onclick="window.location.href=\'./articleEdit.html?articleId='+articleId+'\';">编辑</button>');
        }
    });
}

// 文章被点赞
function articleAgree(articleId) {
    let dom = $(".fa-thumbs-o-up");
    if(dom.length <= 0) {
        toastr.info("you are already agree this!");//已经点赞了
        return;
    }
    let num_arr = dom.parent().text().split(' ');//点赞数加一
    dom.parent().html('<i class="fa fa-thumbs-up"></i>' + String(Number(num_arr[0])+1) + ' ' + num_arr[1] );
    $.ajax({
        url: "article/insertArticleAgree",
        type: "GET",
        data: {"articleId": articleId},
        success:function () {
            toastr.success("agree successful!");
        }
    });
}

// 收藏文章
function articleCollection(articleId) {
    let dom = $(".fa-heart-o");
    if(dom.length <= 0){
        toastr.info("you are already collect this!");//已经收藏了
        return;
    }
    let num_arr = dom.parent().text().split(' ');//收藏数加一
    dom.parent().html('<i class="fa fa-heart"></i>' + String(Number(num_arr[0])+1) + ' ' + num_arr[1] );
    $.ajax({
        url: "article/insertArticleCollect",
        type: "GET",
        data: {"articleId": articleId},
        success: function () {
            toastr.success("collect successful!");
        }
    })
}

// 获取对应的评论列表，并加载
function getComments(articleId){
    $.ajax({
        url: "article/getComment",
        method: "GET",
        data: {"articleId":articleId},
        success: function (data) {
            $(".comment_num").text(data.length + " Comments");
            let dom = $("#comment_list");
            dom.empty();
            for(let comment of data){
                dom.append('<li class="media commentBody">'+'' +
                '<div class="post-comment"><div class="media-body">' +
                '<span><i class="fa fa-user"></i>Posted by <a href="./userHome.html?username='+comment.username+'" target="_blank">' + comment.username + '</a></span>' +
                '<p>' + marked(comment.content) + '</p>' +
                '<ul class="nav navbar-nav post-nav">' +
                    '<li><a href="javascript:void(0);"><i class="fa fa-clock-o"></i>' + (new Date(comment.createTime)).toLocaleString() + '</a></li>' +
                    '<li ><a class="btn reply" href="javascript:void(0)" onclick="replay(this,'+comment.commentId+');"><i class="fa fa-reply"></i>Reply</a></li>' +
                '</ul></div></div></li>');
           }
        },
        error: function (data) {
           toastr.error("fetch the comments fail! "+JSON.stringify(data),"fail");
       }
    });
}

// 向服务器发送增加的评论
function sendComment(comment) {
    $.ajax({
        url:"./article/insertComment",
        type:"POST",
        data: JSON.stringify(comment),
        contentType:"application/json;charset=utf-8",
        success:function(data){
            if(data !== undefined && data !== {}){//成功返回插入后的comment
                toastr.success("add comment success! ");
                $("#comment_list").append('<li class="media commentBody">'+'' +
                    '<div class="post-comment"><div class="media-body">' +
                    '<span><i class="fa fa-user"></i>Posted by <a href="./userHome.html?username='+data.username+'" target="_blank">' + data.username + '</a></span>' +
                    '<p>' + marked(data.content) + '</p>' +
                    '<ul class="nav navbar-nav post-nav">' +
                    '<li><a href="javascript:void(0);"><i class="fa fa-clock-o"></i>' + (new Date(data.createTime)).toLocaleString() + '</a></li>' +
                    '<li ><a class="btn reply" href="javascript:void(0)" onclick="replay(this,'+data.commentId+');"><i class="fa fa-reply"></i>Reply</a></li>' +
                    '</ul></div></div></li>');
                let num_arr=$(".comment_num").text().split(' ');//把评论的个数加一
                $(".comment_num").text( String(Number(num_arr[0])+1)+ " " + num_arr[1] );
            }
            else{
                toastr.error("user not login","fail");
            }
        },
        error(data){
            toastr.error("add comment fail! Network error! "+JSON.stringify(data),"fail");
        }
    });
}

// 评论文章
function comment2article() {
    let commentEntity = {};
    commentEntity.content= $(".commentInput").val();
    if(commentEntity.content == null || commentEntity.content === ""){
        toastr.warning("content could not be null!");
        return;
    }
    commentEntity.articleId = $(".post-title").attr("id");
    $(".commentInput").val("");
    sendComment(commentEntity);
}

// 回复评论按钮按下时，增加对应的输入框
function replay(dom,commentId) {
    $("#reply").remove();
    dom.parentElement.parentElement.parentElement.innerHTML+=(
        '<div class="reply-body col-md-11 col-sm-12 text-right" id="reply">' +
        '<textarea class="form-control replyInput" style="margin: 20px 1px" rows="3" cols="15"></textarea>' +
        '<button class="btn btn-primary" onclick="comment2comment(' + commentId + ')">submit</button>' +
        '</div>');
}

// 评论回复评论
function comment2comment(commentId) {
    let commentEntity = {};
    commentEntity.content= $(".replyInput").val();
    commentEntity.refId = commentId;
    if(commentEntity.content == null || commentEntity.content === ""){
        toastr.warning("content could not be null!");
        return;
    }
    commentEntity.articleId = $(".post-title").attr("id");
    $("#reply").remove();
    sendComment(commentEntity);
}


/**
 *  articleEdit.html 需要的函数
 */

// 文章编辑页面的加载 （修改文章的时候把原文章加载出来）
function articleEditInIt() {
    $.ajax({
        url: "./article/category",
        type: "GET",
        success: function (categorys) {
            let dom = $("#category");
            for(let item of categorys){
                dom.append('<option value="' +item.categoryId +'">' + item.name + '</option>');
            }//首先得到分类并加载
            //如果是在编辑文章，就把这个文章加载出来
            let param = getRequest();
            if(param["articleId"] == null) {
                $("#submit").attr("onclick",'articleSubmit("insertArticle")');
                return;
            }
            else $("#submit").attr("onclick",'articleSubmit("updateArticle")');
            $.ajax({
                url: "./article/getSingleArticle",
                type: "GET",
                data: {"articleId":param["articleId"]},
                success: function (article) {
                    if(article == null || article === {}){//如果是获得的文章不存在，还是按照insert 处理
                        $("#submit").attr("onclick",'articleSubmit("insertArticle")');
                        return;
                    }
                    articleEntry = article; //保存原来的文章对象
                    $("#title").val(article.title);
                    $("#category").val(article.categoryId);
                    acen_edit.insert(article.content);
                }
            });
        },
        error: function (data) {
            toastr("fetch the article category fail! "+JSON.stringify(data),"fail");
        }
    });// 得到分类列表并加载
}

// 文章的提交
function articleSubmit(url) {
    articleEntry.title = $("#title").val();
    articleEntry.categoryId = $("#category").val();
    articleEntry.content = acen_edit.getValue();
    if(articleEntry.title == null || articleEntry.title === ""){
        toastr.error("article title could not be null!");
        return;
    }
    if(articleEntry.categoryId === undefined || articleEntry.categoryId == null){
        toastr.error("please choose a category!");
        return;
    }
    if(articleEntry.content == null || articleEntry.content === ""){
        toastr.error("article content could not be null!");
        return;
    }
    $.ajax({
        url: "./article/"+url,
        type: "POST",
        contentType:"application/json;charset=utf-8",
        data: JSON.stringify(articleEntry),
        success: function (articleId) {
            if(articleId === -1){
                toastr.error("submit Fail");
                return;
            }
            toastr.success("submit success!");
            window.location.href = "./articleDetail.html?articleId="+articleId;
        },
        error: function (data) {
            toastr.error("submit fail! " + JSON.stringify(data),"fail");
        }
    });
}



// 文章推荐列表
function articleRecommend() {
    let dom = $("#recommend");
    if(dom.length === 0)return;
    $.ajax({
        url: "article/getArticleRecommend",
        type: "GET",
        success: function(articles){
            for(let article of articles){
                dom.append('<li><a href="./articleDetail.html?articleId='+article.articleId+'" target="_blank">'
            +'<span style="font-size: larger">'+article.title+'</span><span class="pull-right" style="color: #449d44">'+ article.clicks +' <small>Clicks</small></span>'+
            '</a><hr style="margin: 0 0 15px 0"/></li>');
            }
        },
        error: function (data) {
            toastr.error("fetch the recommend articles fail! "+JSON.stringify(data),"fail");
        }
    });
}