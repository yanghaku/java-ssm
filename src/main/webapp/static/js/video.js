const video_page_size = 24;//默认每页的视频数
const video_max_show_page = 18; // 分页的栏最多展示的个数

// 获取分页的数目，初始化分页的栏
function initVideoListPage(){
    $.ajax({
        url: "video/videoCount",
        type: "GET",
        success: function(num) {
            video_max_page = Math.ceil(num / video_page_size);
            video_now_page = 1;
            // 初始化分页的栏
            let dom = $(".pagination");
            dom.empty();
            dom.append('<li><a href="javascript:void(0)" onclick="changeVideoPage(\'left\');">left</a></li>');//left
            for (let i = 1; i <= video_max_page && i<=video_max_show_page; ++i) {
                dom.append('<li ' + (i === video_now_page ? 'class="active"' : '') + '><a href="javascript:void(0)" ' +
                    'onclick="changeVideoPage(' + i + ');">' + i + '</a></li>');
            }
            dom.append('<li><a href="javascript:void(0)" onclick="changeVideoPage(\'right\')">right</a></li>');//right
            changeVideoPage(video_now_page);
        },
        error: function (data) {
            toastr.error("get page number Error!! "+JSON.stringify(data));
        }
    });
}


// 改变当前显示的页数
function changeVideoPage(page) {
    let list = $(".pagination").children();
    if(page === "left" || page === "right"){
        let range_L = list[1].children[0].text;
        if(range_L === "right"){// 最大页数为 0 的时候
            toastr.warning("no page!");
            return;
        }
        range_L = Number(range_L);
        let range_R = range_L + video_max_show_page; //确定当前的范围 [range_L,range_R)
        if(page === "left"){
            if(range_L === 1){
                toastr.info("No page change!");
                return;
            }
            for(let i=1; i+1<list.length;++i){
                list[i].children[0].text = range_L + i - 2;
                list[i].children[0].setAttribute("onclick",'changeVideoPage('+ (range_L + i -2) + ')');
                if(list[i].className === "active"){
                    if(i+1 === list.length-1) {//如果下一个元素是 right 了
                        now_page = now_page - 1;
                        getVideoList(now_page);
                    }
                    else { // 否则正常更新到下一个节点去
                        list[i].className = "";
                        list[i + 1].className = "active";
                    }
                }
            }
        }
        else{//right
            if(range_R > video_max_page){
                toastr.info("No page change!");
                return;
            }
            for(let i=1; i+1 < list.length; ++i){
                list[i].children[0].text = range_L + i;
                list[i].children[0].setAttribute("onclick",'changeVideoPage('+ (range_L + i) + ')');
                if(list[i].className === "active"){
                    if(i===1){//不能再向左推的时候
                        now_page = now_page + 1;
                        getVideoList(now_page);
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
        getVideoList(page);
    }
}

// 获取当前页数的video 列表
function getVideoList(page) {
    let start = (page-1)*video_page_size;//偏移量从0开始
    if(isNaN(start))return;
    $.ajax({
        url:"video/videoList",
        type: "GET",
        data: {"start":start,"size": video_page_size},
        success: function (videos) {
            insertVideoList(videos);
        },
        error: function (data) {
            toastr.error("fetch the video list fail! "+JSON.stringify(data));
        }
    })
}


// 将获取到的video列表，渲染到dom里
function insertVideoList(videos) {
    let dom = $("#videoList");
    dom.empty();
    for(let video of videos){
        dom.append('<div class="col-md-3 col-sm-12 col-lg-3" style="margin-bottom: 20px"><a href="./videoDetail.html?videoId='+ video.videoId +'" target="_blank">'+
            '<img alt="" class="img-responsive" src="'+video.description+'"/><h3>'+video.videoName+'</h3>'+
            '</a></div>');
    }
}




function getSingleVideo() {
    let param =getRequest();
    let videoId = param["videoId"];
    if(videoId === undefined){
        toastr.error("request param Error! ");
        return;
    }
    $.ajax({
        url: "video/getSingleVideo",
        type: "GET",
        data: {"videoId": videoId},
        success: function (video) {
            $("title").text(video.videoName + " -- 视频观看");
            $("#videoContent").append('<h2>' + video.videoName +'</h2><hr/>' +
                '<p><span><i class="fa fa-clock-o"></i>Create Time: '+ (new Date(video.createTime)).toLocaleString() +
                '</span>  Label: '+video.label+'</p>'+
                '<div  class="embed-responsive embed-responsive-16by9">'+
                '<video width="720" height="640" controls>' +
                '<source src="' + video.videoAddress +'" type="video/mp4">' +
                '您的浏览器不支持Video标签。</video></div>');

            console.log(JSON.stringify(video));
        },
        error: function (data) {
            toastr.error("fetch the video Error! "+JSON.stringify(data));
        }
    })
}
