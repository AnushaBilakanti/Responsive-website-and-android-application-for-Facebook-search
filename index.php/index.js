//retrieved albums and posts
var post_url;
var post_name;
var response_got={};
var typeForFav;
var lati;
var longi;
var options = {
              enableHighAccuracy: true,
              timeout: 5000,
              maximumAge: 0
        };

        function success(pos) 
        {
            var crd = pos.coords;

            lati=`${crd.latitude}`;
            longi=`${crd.longitude}`;
        };
        function error(err) 
        {
            console.warn(`ERROR(${err.code}): ${err.message}`);
        };
navigator.geolocation.getCurrentPosition(success, error, options);
window.fbAsyncInit = function() {
            FB.init({
                appId: '282689898841018',
                version: 'v2.8'
            });
            /*FB.AppEvents.logPageView();*/
        };

        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) {
                return;
            }
            js = d.createElement(s);
            js.id = id;
            js.src = "https://connect.facebook.net/en_US/sdk.js";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));

function Postinfb()
    {
        
         FB.ui({
                app_id: '282689898841018',
                method: 'feed',
                link: window.location.href,
                picture: post_url,
                name: post_name,
                caption: 'FB SEARCH FROM USC CSCI571',
                }, 
                function(response) 
                {
                    if (response && !response.error_message)
                        alert("Posted Successfully");
                    else
                        alert("Not Posted");
                });

    }

$(document).ready(function()
{

   
    var users={};
    var pages={};
    var events={};
    var places={};
    var groups={};

    var username;
    
    $('#dispTable').hide();
    $('#prevPage').hide();
    $('#nextPage').hide();
    $("#albumPanel").hide();
    $("#postPanel").hide();
    $("#accordion").hide();
    $("#detailsPanel").hide();
    $("#detailsPanelPost").hide();
    $('.progress').hide();
    $(".buttons").hide();
    var response_url;
    var response_url_nxt;
    var url_for_posts;//saving the url for images to be 
    // var typeForFav;//used in details when we click on fav icon
    document.getElementById("search_btn").addEventListener("click", retrieve_contents);
    document.getElementById("clear").addEventListener("click", reset);

    $(document).on("click","#nextPage",function()
    {
        
        dispItems(response_url_nxt);
    });

    $(document).on("click","#prevPage",function()
    {
       
        dispItems(response_url);
    });

    function dispItems(url)
    {
        
        // $('.progress').show();
        $('#dispTable').hide();
        $(".buttons").hide();
        $('#prevPage').hide();
        $('#nextPage').hide();
        $.ajax({
            url: 'index.php',
            type: 'GET',
            data: {'url_id':url,section:'loop'},
            success:function(response)
            {
                $('.progress').hide();
                response_got=response;
                response_got=jQuery.parseJSON(response_got);
                // console.log(response);
                response=jQuery.parseJSON(response);
                if(response)
                {
                $("#dispTable").empty();
                $('#dispTable').show();
                $('#prevPage').hide();
                $('#nextPage').hide();
                $('.back').hide();
                $(".postFav").hide();
                $(".favdetail").hide();
                var len = response.data.length;
                var txt = ""; 
                var count=1;
                if(len > 0)
                {
                    txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";
                    for(var i=0;i<len;i++)
                    {
                        if(response.data[i].picture.data.url && response.data[i].name )
                        {
                            idDetail=response.data[i].id;
                            txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm'>";
                            url_for_posts=response.data[i].picture.data.url;
                            if(localStorage.getItem(idDetail))
                            {
                                txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                            }
                            else
                            {
                                txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                            }
                        }
                        count++;
                    }
                    txt += "</tbody>";
                    if(txt!="")
                    {
                        $("#dispTable").append(txt);
                    }
                }
                if(response.paging.next)
                {
                    if(count<25)
                    {
                        $('#nextPage').hide();
                    }
                    else
                    {
                        
                        $('#nextPage').show();
                        response_url_nxt=response.paging.next;
                    }
                    
                   
                }

                if(response.paging.previous)
                {
                   
                    $('#prevPage').show();
                    response_url=response.paging.previous;
                   
                }
            }
            $(".favButton").click(function()
            {
                $('.progress').hide();
                var len1 = response.data.length;
                var fav_id=$(this).attr('id');
                var values;
                //first i check if it does not exist, then i push into localStorage 
                if(localStorage.getItem(fav_id)==null)
                {
                    for(var i=0;i<len1;i++)
                    {
                        if(response.data[i].picture.data.url && response.data[i].name )
                            {
                                idDetail=response.data[i].id;
                                // txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"
                                // +response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+"><span class='glyphicon glyphicon-star-empty'></span></button></td><td><button type='button' class='btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+" style='outline:0;'>";
                                
                                url_for_posts=response.data[i].picture.data.url;
                                if(localStorage.getItem(idDetail))
                                {
                                    txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                                else
                                {
                                    txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                            }
                            count++;
                    }
                    //  Object.keys(localStorage).forEach(function(key){
                    //     alert("key"+key);
                    // console.log(localStorage.getItem(key));
                    // }); 
                }
                else if(localStorage[fav_id])
                {
                    $(this).children().removeClass('glyphicon-star');
                    $(this).children().addClass('glyphicon-star-empty');
                    $(this).children().css("color","black");
                    localStorage.removeItem(fav_id);
                }
            });


            },
            error:function(data)
            {
                $('.progress').hide();
                alert("error");
            }
        });     
    } 

    function retrieve_contents()
    {
        if(document.getElementById("keyword").value!="")
        {
                typeForFav="user";
              
                var idDetail;
                username=$("#keyword").val();
                $('.progress').show();
                $('#dispTable').hide();
                $(".buttons").hide();
                $('#prevPage').hide();
                $('#nextPage').hide();
                $.ajax({
                    url: 'index.php',
                    type: 'GET',
                    data: {name:username,type:'user',section:'disp'},
                    success:function(response)
                    {
                        $('.progress').hide();
                        response_got=response;
                        response_got=jQuery.parseJSON(response_got);
                        response=jQuery.parseJSON(response);
                        if(response)
                        {
                            $('#prevPage').hide();
                            $('#nextPage').hide();
                            $('#dispTable').show();
                            $("#albumPanel").hide();
                            $("#postPanel").hide();
                            $("#detailsPanel").hide();
                            $("#detailsPanelPost").hide();
                            $(".back").hide();
                            $(".postFav").hide();
                            $(".favdetail").hide();
                            var len = response.data.length;
                            var txt = "";
                            $("#dispTable").empty();
                            var count=1;
                            if(len > 0)
                            {
                                txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";
                                for(var i=0;i<len;i++)
                                {
                                    if(response.data[i].picture.data.url && response.data[i].name )
                                    {
                                        idDetail=response.data[i].id;
                                        // txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"
                                        // +response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+"><span class='glyphicon glyphicon-star-empty'></span></button></td><td><button type='button' class='btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                        txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+" style='outline:0;'>";
                                        
                                        url_for_posts=response.data[i].picture.data.url;
                                        if(localStorage.getItem(idDetail))
                                        {
                                            txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                        }
                                        else
                                        {
                                            txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                        }
                                    }
                                    count++;
                                }
                                if(txt!="")
                                {
                                    txt += "</tbody>";
                                    $("#dispTable").append(txt);
                                }
                                if(response.paging.next)
                                {
                                    $('#nextPage').show();
                                    response_url_nxt=response.paging.next;
                                }
                                if(response.paging.previous)
                                {
                                    $('#prevPage').show();
                                    response_url=response.paging.previous;
                                }
                            }
                            $(".favButton").click(function()
                            {
                                var len1 = response.data.length;
                                var fav_id=$(this).attr('id');
                                var values;
                                //first i check if it does not exist, then i push into localStorage 
                                if(localStorage.getItem(fav_id)==null)
                                {
                                    for(var i=0;i<len1;i++)
                                    {
                                        if(response.data[i].id==fav_id)
                                        {
                                            values=['user',response.data[i].picture.data.url,response.data[i].name];
                                            localStorage.setItem(fav_id,JSON.stringify(values));
                                            $(this).children().removeClass('glyphicon-star-empty');
                                            $(this).children().addClass('glyphicon-star');
                                            $(this).children().css("color","yellow");
                                            break;   
                                        }
                                    }
                                    
                                }
                                else if(localStorage[fav_id])
                                {
                                    $(this).children().removeClass('glyphicon-star');
                                    $(this).children().addClass('glyphicon-star-empty');
                                    $(this).children().css("color","black");
                                    localStorage.removeItem(fav_id);
                                }
                            });
                            
                        }
                        
                    },
                    error:function(data)
                    {
                        $('.progress').hide();
                        alert("error");
                    }
                });
        }

    }
    $('#user').click(function() 
    {
        typeForFav="user";
        // $('.progress').show();
        $('#dispTable').hide();
        $(".buttons").hide();
        $('#prevPage').hide();
        $('#nextPage').hide();
        $.ajax({
            url: 'index.php',
            type: 'GET',
            data: {name:username,type:'user',section:'disp'},
            success:function(response)
            {
                $('.progress').hide();
                response_got=response;
                response_got=jQuery.parseJSON(response_got);
                var idDetail;
                response=jQuery.parseJSON(response);
                if(response)
                {
                    $('#prevPage').hide();
                    $('#nextPage').hide();
                    $('#dispTable').show();
                    $("#albumPanel").hide();
                    $("#postPanel").hide();
                    $("#detailsPanel").hide();
                    $("#detailsPanelPost").hide();
                    $(".back").hide();
                    $(".postFav").hide();
                    $(".favdetail").hide();
                    var len = response.data.length;
                    var txt = "";
                    var count=1;
                    if(len > 0)
                    {
                        $("#dispTable").empty();
                        txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";

                        for(var i=0;i<len;i++)
                        {
                            if(response.data[i].picture.data.url && response.data[i].name )
                            {
                                idDetail=response.data[i].id;
                                // txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"
                                // +response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+"><span class='glyphicon glyphicon-star-empty'></span></button></td><td><button type='button' class='btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+" style='outline:0;'>";
                                
                                url_for_posts=response.data[i].picture.data.url;
                                if(localStorage.getItem(idDetail))
                                {
                                    txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                                else
                                {
                                    txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                            }
                            count++;
                        }
                        txt += "</tbody>";
                        if(txt!="")
                        {
                            $("#dispTable").append(txt);
                        }
                    }
                    if(response.paging.next)
                    {
                        if(count<25)
                        {
                            $('#nextPage').hide();
                        }
                        else
                        {
                            $('#nextPage').show();
                            response_url_nxt=response.paging.next;
                        }
                        
                    }
                    if(response.paging.previous)
                    {
                        $('#prevPage').show();
                        response_url=response.paging.previous;
                    }
                    $(".favButton").click(function()
                    {
                        $('.progress').hide();
                        var len1 = resp.data.length;
                        var fav_id=$(this).attr('id');
                        var values;
                        //first i check if it does not exist, then i push into localStorage 
                        if(localStorage.getItem(fav_id)==null)
                        {
                            for(var i=0;i<len1;i++)
                            {
                                if(response.data[i].id==fav_id)
                                {
                                    values=['user',response.data[i].picture.data.url,response.data[i].name];
                                    localStorage.setItem(fav_id,JSON.stringify(values));
                                    $(this).children().removeClass('glyphicon-star-empty');
                                    $(this).children().addClass('glyphicon-star');
                                    $(this).children().css("color","yellow");
                                    break;   
                                }
                            }
                            //  Object.keys(localStorage).forEach(function(key){
                            //     alert("key"+key);
                            // console.log(localStorage.getItem(key));
                            // }); 
                        }
                        else if(localStorage[fav_id])
                        {
                            $(this).children().removeClass('glyphicon-star');
                            $(this).children().addClass('glyphicon-star-empty');
                            $(this).children().css("color","black");
                            localStorage.removeItem(fav_id);
                        }
                    });   
                }
            },
            error:function(data)
            {
                $('.progress').hide();
                alert("error");
            }
        });
    });
    $('#page').click(function() 
    {
        typeForFav="page";
        // $('.progress').show();
        $('#dispTable').hide();
        $(".buttons").hide();
        $('#prevPage').hide();
        $('#nextPage').hide();
        $.ajax({
            url: 'index.php',
            type: 'GET',
            data: {name:username,type:'page',section:'disp'},
            success:function(response)
            {
                $('.progress').hide();
                response_got=response;
                var idDetail;
                response=jQuery.parseJSON(response);
                if(response)
                {
                    $('#prevPage').hide();
                    $('#nextPage').hide();
                    $('#dispTable').show();
                    $("#albumPanel").hide();
                    $("#postPanel").hide();
                    $("#detailsPanel").hide();
                    $("#detailsPanelPost").hide();
                    $(".back").hide();
                    $(".postFav").hide();
                    $(".favdetail").hide();
                    var len = response.data.length;
                    var txt = "";
                    var count=1;
                    if(len > 0)
                    {
                        $("#dispTable").empty();
                        txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";
                        for(var i=0;i<len;i++)
                        {
                            idDetail=response.data[i].id;
                            if(response.data[i].picture.data.url && response.data[i].name )
                            {
                                idDetail=response.data[i].id;
                                // txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"
                                // +response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+"><span class='glyphicon glyphicon-star-empty'></span></button></td><td><button type='button' class='btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+" style='outline:0;'>";
                                
                                url_for_posts=response.data[i].picture.data.url;
                                if(localStorage.getItem(idDetail))
                                {
                                    // alert("localStorage.getItem(idDetail)-if"+localStorage.getItem(idDetail));
                                    txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                                else
                                {
                                    // alert("localStorage.getItem(idDetail)-else"+localStorage.getItem(idDetail));
                                    txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                            }
                            count++;
                        }
                        txt += "</tbody>";
                        if(txt!="")
                        {
                            $("#dispTable").append(txt);
                        }
                    }
                    if(response.paging.next)
                    {
                        if(count<25)
                        {
                            $('#nextPage').hide();
                        }
                        else
                        {
                             $('#nextPage').show();
                            response_url_nxt=response.paging.next;
                        }
                       
                    
                    }
                    if(response.paging.previous)
                    {
                        $('#prevPage').show();
                        response_url=response.paging.previous;
                    }
                    $(".favButton").click(function()
                    {
                        $('.progress').hide();
                        var len1 = response.data.length;
                        var fav_id=$(this).attr('id');
                        var values;
                        //first i check if it does not exist, then i push into localStorage 
                        if(localStorage.getItem(fav_id)==null)
                        {
                            for(var i=0;i<len1;i++)
                            {
                                if(response.data[i].id==fav_id)
                                {
                                    values=['page',response.data[i].picture.data.url,response.data[i].name];
                                    localStorage.setItem(fav_id,JSON.stringify(values));
                                    $(this).children().removeClass('glyphicon-star-empty');
                                    $(this).children().addClass('glyphicon-star');
                                    $(this).children().css("color","yellow");
                                    break;   
                                }
                            }
                            //  Object.keys(localStorage).forEach(function(key){
                            //     alert("key"+key);
                            // console.log(localStorage.getItem(key));
                            // }); 
                        }
                        else if(localStorage[fav_id])
                        {
                            $(this).children().removeClass('glyphicon-star');
                            $(this).children().addClass('glyphicon-star-empty');
                            $(this).children().css("color","black");
                            localStorage.removeItem(fav_id);
                        }
                    });
                }
            },//success
            error:function(data)
            {
                $('.progress').hide();
                alert("error");
            }
        });
    });
    $('#event').click(function() 
    {
        typeForFav="event";
        // $('.progress').show();
        $('#dispTable').hide();
        $(".buttons").hide();
        $('#prevPage').hide();
        $('#nextPage').hide();
        $.ajax({
            url: 'index.php',
            type: 'GET',
            data: {name:username,type:'event',section:'disp'},
            success:function(response)
            {
                $('.progress').hide();
                response_got=response;
                response=jQuery.parseJSON(response);
                if(response)
                {
                    $('#prevPage').hide();
                    $('#nextPage').hide();
                    $('#dispTable').show();
                    $("#albumPanel").hide();
                    $("#postPanel").hide();
                    $("#detailsPanel").hide();
                    $("#detailsPanelPost").hide();
                    $(".back").hide();
                    $(".postFav").hide();
                    $(".favdetail").hide();
                    var len = response.data.length;
                    var txt = "";
                    var count=1;
                    if(len > 0)
                    {
                        $("#dispTable").empty();
                        txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";
                        for(var i=0;i<len;i++)
                        {
                            if(response.data[i].picture.data.url && response.data[i].name )
                            {
                                idDetail=response.data[i].id;
                                // txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"
                                // +response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+"><span class='glyphicon glyphicon-star-empty'></span></button></td><td><button type='button' class='btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+" style='outline:0;'>";
                                
                                url_for_posts=response.data[i].picture.data.url;
                                if(localStorage.getItem(idDetail))
                                {
                                    txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                                else
                                {
                                    txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                            }
                            count++;
                        }
                        txt += "</tbody>";
                        if(txt!="")
                        {
                            $("#dispTable").append(txt);
                        }
                        if(response.paging.next)
                        {
                            if(count<25)
                            {
                                $('#nextPage').hide();
                            }
                            else
                            {
                               $('#nextPage').show();
                                response_url_nxt=response.paging.next; 
                            }
                            
                        }
                        if(response.paging.previous)
                        {
                            $('#prevPage').show();
                            response_url=response.paging.previous;
                        }
                    }
                    $(".favButton").click(function()
                    {
                        $('.progress').hide();
                        var len1 = response.data.length;
                        var fav_id=$(this).attr('id');
                        var values;
                        //first i check if it does not exist, then i push into localStorage 
                        if(localStorage.getItem(fav_id)==null)
                        {
                            for(var i=0;i<len1;i++)
                            {
                                if(response.data[i].id==fav_id)
                                {
                                    values=['event',response.data[i].picture.data.url,response.data[i].name];
                                    localStorage.setItem(fav_id,JSON.stringify(values));
                                    $(this).children().removeClass('glyphicon-star-empty');
                                    $(this).children().addClass('glyphicon-star');
                                    $(this).children().css("color","yellow");
                                    break;   
                                }
                            }
                            //  Object.keys(localStorage).forEach(function(key){
                            //     alert("key"+key);
                            // console.log(localStorage.getItem(key));
                            // }); 
                        }
                        else if(localStorage[fav_id])
                        {
                            $(this).children().removeClass('glyphicon-star');
                            $(this).children().addClass('glyphicon-star-empty');
                            $(this).children().css("color","black");
                            localStorage.removeItem(fav_id);
                        }
                    });
                }
            },
            error:function(data)
            {
                $('.progress').hide();
                alert("error");
            }
        });
    });
    $('#place').click(function() 
    {
        
        typeForFav="place";
        // $('.progress').show();
        $('#dispTable').hide();
        $(".buttons").hide();
        $('#prevPage').hide();
        $('#nextPage').hide();
        var latitude,longitude;

// navigator.geolocatiosn.getCurrentPosition(success, error, options);


        $.ajax({
            url: 'index.php',
            type: 'GET',
            data: {name:username,type:'place',section:'disp',latitude:lati,longitude:longi},
            success:function(response)
            {
                $('.progress').hide();
                response_got=response;
                response_got=jQuery.parseJSON(response_got);
                response=jQuery.parseJSON(response);
                if(response)
                {
                    $('#prevPage').hide();
                    $('#nextPage').hide();
                    $('#dispTable').show();
                    $("#albumPanel").hide();
                    $("#postPanel").hide();
                    $("#detailsPanel").hide();
                    $("#detailsPanelPost").hide();
                    $(".back").hide();
                    $(".postFav").hide();
                    $(".favdetail").hide();
                    var len = response.data.length;
                    var txt = "";
                    var count=1;
                    if(len > 0)
                    {
                        $("#dispTable").empty();
                        txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";
                        for(var i=0;i<len;i++)
                        {
                            if(response.data[i].picture.data.url && response.data[i].name )
                            {
                                idDetail=response.data[i].id;
                                // txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"
                                // +response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+"><span class='glyphicon glyphicon-star-empty'></span></button></td><td><button type='button' class='btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+" style='outline:0;'>";
                                
                                url_for_posts=response.data[i].picture.data.url;
                                if(localStorage.getItem(idDetail))
                                {
                                    txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                                else
                                {
                                    txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                            }
                            count++;
                        }
                        txt += "</tbody>";
                        if(txt!="")
                        {
                            $("#dispTable").append(txt);
                        }
                        if(response.paging.next)
                        {
                            if(count<25)
                            {
                                $('#nextPage').hide();
                            }
                            else
                            {
                                $('#nextPage').show();
                                response_url_nxt=response.paging.next;
                            }
                               
                        }
                        if(response.paging.previous)
                        {
                            $('#prevPage').show();
                            response_url=response.paging.previous;
                        }
                    }
                    $(".favButton").click(function()
                    {
                        $('.progress').hide();
                        var len1 = response.data.length;
                        var fav_id=$(this).attr('id');
                        var values;
                        //first i check if it does not exist, then i push into localStorage 
                        if(localStorage.getItem(fav_id)==null)
                        {
                            for(var i=0;i<len1;i++)
                            {
                                if(response.data[i].id==fav_id)
                                {
                                    values=['place',response.data[i].picture.data.url,response.data[i].name];
                                    localStorage.setItem(fav_id,JSON.stringify(values));
                                    $(this).children().removeClass('glyphicon-star-empty');
                                    $(this).children().addClass('glyphicon-star');
                                    $(this).children().css("color","yellow");
                                    break;   
                                }
                            }
                            //  Object.keys(localStorage).forEach(function(key){
                            //     alert("key"+key);
                            // console.log(localStorage.getItem(key));
                            // }); 
                        }
                        else if(localStorage[fav_id])
                        {
                            $(this).children().removeClass('glyphicon-star');
                            $(this).children().addClass('glyphicon-star-empty');
                            $(this).children().css("color","black");
                            localStorage.removeItem(fav_id);
                        }
                    });
                }
            },
            error:function(data)
            {
                $('.progress').hide();
                alert("error");
            }
        });
    });
    $('#group').click(function() 
    {
        typeForFav="group";
        // $('.progress').show();
        $('#dispTable').hide();
        $(".buttons").hide();
        $('#prevPage').hide();
        $('#nextPage').hide();
        $.ajax({
            url: 'index.php',
            type: 'GET',
            data: {name:username,type:'group',section:'disp'},
            success:function(response)
            {
                $('.progress').hide();
                response_got=response;
                response_got=jQuery.parseJSON(response_got);
                response=jQuery.parseJSON(response);
                if(response)
                {
                    $('#prevPage').hide();
                    $('#nextPage').hide();
                    $('#dispTable').show();
                    $("#albumPanel").hide();
                    $("#postPanel").hide();
                    $("#detailsPanel").hide();
                    $("#detailsPanelPost").hide();
                    $(".back").hide();
                    $(".postFav").hide();
                    $(".favdetail").hide();
                    var len = response.data.length;
                    var txt = "";
                    var count=1;
                    if(len > 0)
                    {
                        $("#dispTable").empty();
                        txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";
                        for(var i=0;i<len;i++)
                        {
                            if(response.data[i].picture.data.url && response.data[i].name )
                            {
                                idDetail=response.data[i].id;
                                // txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"
                                // +response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+"><span class='glyphicon glyphicon-star-empty'></span></button></td><td><button type='button' class='btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+" style='outline:0;'>";
                                

                                url_for_posts=response.data[i].picture.data.url;
                                if(localStorage.getItem(idDetail))
                                {
                                    txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                                else
                                {
                                    txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                                }
                            }
                            count++;
                        }
                        txt += "</tbody>";
                        if(txt!="")
                        {
                            $("#dispTable").append(txt);
                        }
                        if(response.paging.next)
                        {
                            if(count<25)
                            {
                                $('#nextPage').hide();
                            }
                            else
                            {
                                $('#nextPage').show();
                                response_url_nxt=response.paging.next;
                            }
                            
                        }
                        if(response.paging.previous)
                        {
                            $('#prevPage').show();
                            response_url=response.paging.previous;
                        }
                    }
                    $(".favButton").click(function()
                    {
                        var len1 = response.data.length;
                        var fav_id=$(this).attr('id');
                        var values;
                        //first i check if it does not exist, then i push into localStorage 
                        if(localStorage.getItem(fav_id)==null)
                        {
                            for(var i=0;i<len1;i++)
                            {
                                if(response.data[i].id==fav_id)
                                {
                                    values=['group',response.data[i].picture.data.url,response.data[i].name];
                                    localStorage.setItem(fav_id,JSON.stringify(values));
                                    $(this).children().removeClass('glyphicon-star-empty');
                                    $(this).children().addClass('glyphicon-star');
                                    $(this).children().css("color","yellow");
                                    break;   
                                }
                            }
                            //  Object.keys(localStorage).forEach(function(key){
                            //     alert("key"+key);
                            // console.log(localStorage.getItem(key));
                            // }); 
                        }
                        else if(localStorage[fav_id])
                        {
                            $(this).children().removeClass('glyphicon-star');
                            $(this).children().addClass('glyphicon-star-empty');
                            $(this).children().css("color","black");
                            localStorage.removeItem(fav_id);
                        }
                    });
                }
            },
            error:function(data)
            {
                $('.progress').hide();
                alert("error");

            }
        });
    });
    
    $('#favorite').click(function() 
    {
        
        $('#prevPage').hide();
        $('#nextPage').hide();
        $('#dispTable').show();
        $("#albumPanel").hide();
        $("#postPanel").hide();
        $(".back").hide();
        $(".postFav").hide();
        $(".favdetail").hide();
        var txt = "";
        var count=1;//caan u heae me
        var name,url,typeTab;
        $("#dispTable").empty();
        txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th>Type</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";
        //loop through local storage
        Object.keys(localStorage).forEach(function(key)
        {
            var value = JSON.parse(localStorage.getItem(key));
            // value = value.split(",");
            typeTab= value[0]; // getting name from split string
            url=value[1];
            name=value[2]; 
            url_for_posts=url;
            txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+url+"' width='40' height='40'></td><td>"+name+"</td><td>"+typeTab+"</td><td><button type='button' class='delFavButton btn btn-default btn-sm' id="+key+"><span class='glyphicon glyphicon-trash'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+key+"><span class='glyphicon glyphicon-chevron-right'></span></button></button></td></tr>";
            count++;
        });
        txt += "</tbody>";
        if(txt!="")
        {
            $("#dispTable").append(txt);
        }
            
    });
    $(document).on("click",".delFavButton",function()
    {
        $('.progress').hide();
        var fav_id=$(this).attr('id');
        $(this).parents('tr').remove();
        if(localStorage[fav_id])
        {
            localStorage.removeItem(fav_id);
        }
        $('#prevPage').hide();
        $('#nextPage').hide();
        $('#dispTable').show();
        $("#albumPanel").hide();
        $("#postPanel").hide();

        var txt = "";
        var count=1;
        var name,url,typeTab;
        $("#dispTable").empty();
        txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th>Type</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";
        //loop through local storage
        Object.keys(localStorage).forEach(function(key)
        {
            var value = JSON.parse(localStorage.getItem(key));
            // value = value.split(",");
            typeTab= value[0]; // getting name from split string
            url=value[1];
            name=value[2]; 
            txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+url+"' width='40' height='40'></td><td>"+name+"</td><td>"+typeTab+"</td><td><button type='button' class='delFavButton btn btn-default btn-sm' id="+key+"><span class='glyphicon glyphicon-trash'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+key+"><span class='glyphicon glyphicon-chevron-right'></span></button></button></td></tr>";
            count++;
        });
        txt += "</tbody>";
        if(txt!="")
        {
            $("#dispTable").append(txt);
        }
    });

    $(document).on("click",".detailButton",function()
    {
        // alert("Event");
        $('.progress').hide();
        //make http call for albums and posts and create albums and posts dynamically
        var detail_id=$(this).attr('id');
        $('#dispTable').hide();
        $('#prevPage').hide();
        $('#nextPage').hide();
        $("#accordion").show();
        $("#albumPanel").show();
        $("#postPanel").show();
        $("#detailsPanel").show();
        $("#detailsPanelPost").show();
        $("#detailsPanel").empty();
        $("#detailsPanelPost").empty();
        $(".panel-group").empty();
        $(".back").show();
        $(".postFav").show();
        $(".favdetail").hide();
        $("#albumtable").empty();
        $("#posttable").empty();
        $(".buttons").show();
        // var txt="";

        // <div class="progress">
        //     <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width:50%">
        //     </div> 
        // </div>
        // var albumprogress="";
        // var postprogress="";

        // var albumprogress+="<div class='progress'><div class='progress-bar progress-bar-striped active' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width:50%'></div></div>";
        // var postprogress+="<div class='progress'><div class='progress-bar progress-bar-striped active' role='progressbar' aria-valuenow='40' aria-valuemin='0' aria-valuemax='100' style='width:50%'></div></div>";

        // $("#albumtable").append(albumprogress);
        // $("#posttable").append(postprogress);

        var txt1="";
        var txt3="";
        var txt4="";
        var albumcount=1;
        $.ajax({
                url: 'index.php',
                type: 'GET',
                data: {id:detail_id,section:'albumpost'},
                success:function(albumresponse)
                {
                    // $(.backbutton).show();
                    // $(.postFav).show();
                    // response_got=jQuery.parseJSON(response_got);
                    var txt="";

                    if(typeForFav=="event")
                    {
                            txt1+="<button type='button' class='back btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-left'>Back</span></button>";
                            $(".backbutton").empty();
                            $(".backbutton").append(txt1); //yes tat is button

                            txt3+="<button type='button' class='btn btn-default btn-sm' onclick='Postinfb()'><img src='http://cs-server.usc.edu:45678/hw/hw8/images/facebook.png' width=10% height=10%/></button>";
                            // $("#post_fb").empty();
                            // $("#post_fb").append(txt3);

                            // txt4+="<button type='button' class='favdetail btn btn-default btn-sm'><span class='glyphicon glyphicon-star' style='color:yellow'></span></button>";
                            txt3+="<button type='button' class='favdetail btn btn-default btn-sm' id='"+detail_id+"'>";

                            if(localStorage.getItem(detail_id))
                            {
                                txt3+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button>";
                            }
                            else
                            {
                                txt3+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button>";
                            }
                            $(".postFav").empty();
                            $(".postFav").append(txt3);

                            var txt="";
                            $("#albumtable").empty();
                            txt += "<div style='background-color:#fff5e6;width:100%;'>No Data Found</div>";

                            if(txt!="")
                            {
                                $("#albumtable").append(txt);
                            }

                            var txt2="";
                            $("#posttable").empty();
                            txt2 += "<div style='background-color:#fff5e6;width:100%;'>No Data Found</div>";

                            if(txt2!="")
                            {
                                $("#posttable").append(txt2);
                            }
                    }
                    else
                    {

                                albumresponse=jQuery.parseJSON(albumresponse);
                                txt1+="<button type='button' class='back btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-left'>Back</span></button>";
                                $(".backbutton").empty();
                                $(".backbutton").append(txt1); //yes tat is button

                                txt3+="<button type='button' class='btn btn-default btn-sm' onclick='Postinfb()'><img src='http://cs-server.usc.edu:45678/hw/hw8/images/facebook.png' width=10% height=10%/></button>";
                                // $("#post_fb").empty();
                                // $("#post_fb").append(txt3);

                                // txt4+="<button type='button' class='favdetail btn btn-default btn-sm'><span class='glyphicon glyphicon-star' style='color:yellow'></span></button>";
                                txt3+="<button type='button' class='favdetail btn btn-default btn-sm' id='"+detail_id+"'>";

                                // console.log("typeof detail_id"+typeof detail_id);
                                // Object.keys(localStorage).forEach(function(key)
                                // {
                                //         // console.log(localStorage.getItem(key));
                                //         // console.log("key"+key);

                                // });
                                // alert("Outside");
                                // alert("localStorage.getItem(idDetail)"+localStorage.getItem(idDetail));
                                if(localStorage.getItem(detail_id))
                                {
                                    txt3+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button>";
                                }
                                else
                                {
                                    txt3+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button>";
                                }
                                $(".postFav").empty();
                                $(".postFav").append(txt3);

                                if(albumresponse.albums)
                                {
                                    var albums=albumresponse.albums;
                                    var len_album=albums.data.length;
                                    
                                    txt+="<div class='panel-group' id='accordion'>";
                                    for(var i=0;i<len_album;i++)
                                    {
                                        var album_id=albums.data[i].id;
                                        // console.log(album_id);

                                        txt+="<div class='panel panel-default'><div class='panel-heading'><h4 class='panel-title'>";
                                        txt+="<a data-toggle='collapse' data-parent='#accordion' href='#"+album_id+"'>"+albums.data[i].name+"</a>";
                                        if(albumcount==1)
                                        {
                                            txt+="</h4></div><div id='"+album_id+"' class='panel-collapse collapse in'><div class='panel-body'><img src='"+albums.data[i].photos.data[0].images[0].source+"' width=100% height=60% style='border-radius:5px;'/></div>"; 
                                        }
                                        else
                                        {
                                            txt+="</h4></div><div id='"+album_id+"' class='panel-collapse collapse'><div class='panel-body'><img src='"+albums.data[i].photos.data[0].images[0].source+"' width=100% height=60% style='border-radius:5px;'/></div>";
                                        }
                                        albumcount++;
                                        txt+="<div class='panel-body'><img src='"+albums.data[i].photos.data[1].images[0].source+"' width=100% height=50% style='border-radius:5px;'/></div></div></div>";
                                        // txt += "<div class='panel panel-default album_individual'><div class='panel heading'><h4 class='panel-title'><a data-toggle='collapse' data-parent='#accordion' href='#"+album_id+"'>"+albums.data[i].name+"</a></h4></div>";
                                        // txt+="<div id='"+album_id+"' class='panel-collapse collapse in'><div class='panel-body'><img src='"+albums.data[i].photos.data[0].picture+"'/></div></div>";
                                        // txt+="<h3>"+albums.data[i].name+"</h3>";
                                        // txt+="<div><img alt='img' src='"+albums.data[i].photos.data[0].picture+"'/></div>"; Photos.data[0].images[0]
                                    }
                                    if(txt!="")
                                    {
                                        $("#albumtable").append(txt);
                                    }
                                }
                                if(!("albums" in albumresponse))
                                {
                                    $("#albumtable").empty();
                                    txt += "<div style='background-color:#fff5e6;width:100%;'>No Data Found</div>";

                                    if(txt!="")
                                    {
                                        $("#albumtable").append(txt);
                                    }

                                }
                                if(albumresponse.posts)
                                {
                                    // console.log("response_got-posts"+response_got);
                                    // console.log("HINJKNAji");
                                    response_got=jQuery.parseJSON(response_got);
                                    var len1 = response_got.data.length;
                                    //console.log("Reslen1"+len1);
                                    var values;
                                    // alert("len1"+response_got.data[0].id);
                                    //first i check if it does not exist, then i push into localStorage 
                                    for(var i=0;i<len1;i++)
                                    {
                                        // console.log("detail_id"+detail_id);
                                        // console.log("response_got.data[i].id"+response_got.data[i].id);
                                        // // alert("typeofresponse_got.data[i].id"+typeof response_got.data[i].id);
                                        // // alert("detail_id"+typeof detail_id);
                                        // console.log("Compare"+typeof response_got.data[0].id);
                                        if(parseInt(response_got.data[i].id,10)==parseInt(detail_id,10))
                                        {
                                            post_url=response_got.data[i].picture.data.url;
                                            // console.log("post_url"+post_url);
                                            post_name=response_got.data[i].name;
                                            // console.log("post_name"+post_name);  
                                        }
                                    }

                                    // alert("ki"+post_name);
                                        //  Object.keys(localStorage).forEach(function(key){
                                        //     alert("key"+key);
                                        // console.log(localStorage.getItem(key));
                                        // }); 
                                    

                                    var posts=albumresponse.posts;
                                    var len_posts=posts.data.length;
                                    var txt5="";
                                    $("#posttable").empty();
                                    for(var i=0;i<len_posts;i++)
                                    {
                                        
                                        txt5+="<div class='panel panel-default'><div class='panel-body'>";
                                        txt5+="<table><tr><td rowspan='2'><img src='"+post_url+"' width=10% height=10%/></td>";
                                        txt5+="<td><b>"+post_name+"</b></td></tr>";
                                        var time=posts.data[i].created_time.slice(0, posts.data[i].created_time.indexOf("+"));
                                        var final_time=time.replace("T"," ");
// ;                                        alert("final_time"+final_time);
                                        txt5+="<tr><td>"+final_time+"</td></tr>";
                                        txt5+="<tr><td colspan='3'>"+posts.data[i].message+"</td></tr></table></div></div>";
                                        // txt2+="<div class='post_img'><img src='"+post_url+"' width=10% height=10%/></div>";
                                        // txt2+="<div class='name_post'><b>"+post_name+"</b></div>";
                                        // txt2+="<div class='time_post'>"+posts.data[i].created_time+"</div>"+posts.data[i].message+"</div></div>"; 
                                    }
                                    if(txt5!="")
                                    {
                                        $("#posttable").append(txt5);
                                    }
                                }
                                if(!("posts" in albumresponse))
                                {
                                    var txt6="";
                                    $("#posttable").empty();
                                    txt6 += "<div style='background-color:#fff5e6;width:100%;'>No Data Found</div>";

                                    if(txt6!="")
                                    {
                                        $("#posttable").append(txt6);
                                    }

                                }                        

                    }//else

                    
                },
                error:function(data)
                {
                    $('.progress').hide();
                    alert("error");

                }
        });
        // $(document).on("click","#back",function()
        //     {
        //         console.log("Back clicked");
        //         $('#dispTable').hide();
        //         $('#prevPage').hide();
        //         $('#nextPage').hide();

        //     });
    });
    
    $(document).on("click",".favdetail",function()
    {
        // response_got=jQuery.parseJSON(response_got);
        var len1 = response_got.data.length;
        var fav_values;
        var idFavDetail=$(this).attr('id');
        if(localStorage.getItem(idFavDetail)==null)
        {
            for(var i=0;i<len1;i++)
            {

                if(response_got.data[i].id==idFavDetail)
                {

                    fav_values=[typeForFav,response_got.data[i].picture.data.url,response_got.data[i].name];
                    localStorage.setItem(idFavDetail,JSON.stringify(fav_values));
                    $(this).children().removeClass('glyphicon-stsar-empty');
                    $(this).children().addClass('glyphicon-star');
                    $(this).children().css("color","yellow");
                    break;   
                }
            }
        }
        else if(localStorage[idFavDetail])
        {
            $(this).children().removeClass('glyphicon-star');
            $(this).children().addClass('glyphicon-star-empty');
            $(this).children().css("color","black");
            localStorage.removeItem(idFavDetail);
        }

    });
    // $('.back').click(function() 
    $(document).on("click",".back",function()
    {
        $('.progress').hide();
        $(".buttons").hide();
        $("#detailsPanel").empty();
        $("#detailsPanelPost").empty();
        $('#dispTable').show();
       

        $('#prevPage').hide();
        $('#nextPage').hide();
        $("#albumPanel").hide();
        $("#postPanel").hide();
        $("#detailsPanel").hide();
        $("#detailsPanelPost").hide();
        $(".back").hide();
        $(".postFav").hide();
        $(".favdetail").hide();
        $(".buttons").hide();
        var len = response_got.data.length;
        var txt = "";
        var count=1;
        if(len > 0)
        {
            $("#dispTable").empty();
            txt+="<thead><tr><th>#</th><th>Profile Photo</th><th>Name</th><th data-field='glyphicons'>Favorite</th><th>Details</th></tr></thead><tbody>";

            for(var i=0;i<len;i++)
            {
                if(response_got.data[i].picture.data.url && response_got.data[i].name )
                {
                    idDetail=response_got.data[i].id;
                    // txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response.data[i].picture.data.url+"' width='40' height='40'></td><td>"
                    // +response.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response.data[i].id+"><span class='glyphicon glyphicon-star-empty'></span></button></td><td><button type='button' class='btn btn-default btn-sm'><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                    txt += "<tr><td>"+count+"</td><td><img class='img-responsive img-circle' src='"+response_got.data[i].picture.data.url+"' width='40' height='40'></td><td>"+response_got.data[i].name+"</td><td><button type='button' class='favButton btn btn-default btn-sm' id="+response_got.data[i].id+" style='outline:0;'>";
                    
                    url_for_posts=response_got.data[i].picture.data.url;
                    if(localStorage.getItem(idDetail))
                    {
                        txt+="<span class='glyphicon glyphicon-star' style='color:yellow'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                    }
                    else
                    {
                        txt+="<span class='glyphicon glyphicon-star-empty' style='color:black'></span></button></td><td><button type='button' class='detailButton btn btn-default btn-sm' id="+idDetail+"><span class='glyphicon glyphicon-chevron-right'></span></button></td></tr>";
                    }
                }
                count++;
            }
            txt += "</tbody>";
            if(txt!="")
            {
                $("#dispTable").append(txt);
            }
        }
        if(response_got.paging.next)
        {
            $('#nextPage').show();
            response_url_nxt=response_got.paging.next;
        }
        if(response_got.paging.previous)
        {
            $('#prevPage').show();
            response_url=response_got.paging.previous;
        }
        $(".favButton").click(function()
        {
            $('.progress').hide();
            var len1 = response_got.data.length;
            var fav_id=$(this).attr('id');
            var values;
            //first i check if it does not exist, then i push into localStorage 
            if(localStorage.getItem(fav_id)==null)
            {
                for(var i=0;i<len1;i++)
                {
                    if(response_got.data[i].id==fav_id)
                    {
                        values=[typeForFav,response_got.data[i].picture.data.url,response_got.data[i].name];
                        localStorage.setItem(fav_id,JSON.stringify(values));
                        $(this).children().removeClass('glyphicon-star-empty');
                        $(this).children().addClass('glyphicon-star');
                        $(this).children().css("color","yellow");
                        break;   
                    }
                }
                //  Object.keys(localStorage).forEach(function(key){
                //     alert("key"+key);
                // console.log(localStorage.getItem(key));
                // }); 
            }
            else if(localStorage[fav_id])
            {
                $(this).children().removeClass('glyphicon-star');
                $(this).children().addClass('glyphicon-star-empty');
                $(this).children().css("color","black");
                localStorage.removeItem(fav_id);
            }
        });   
    });
    // $(document).on("click","#back",function()
    // {
    //     console.log("Back clicked");
    //     $('#dispTable').hide();
    //     $('#prevPage').hide();
    //     $('#nextPage').hide();

    // });
    function reset()
    {
        $('.progress').hide();
        $('#dispTable').hide();
        $('#prevPage').hide();
        $('#nextPage').hide();
        $("#albumPanel").hide();
        $("#postPanel").hide();
        $("#detailsPanel").hide();
        $("#detailsPanelPost").hide();
        $('#keyword').val('');
        $('.buttons').hide();
        $('.backbutton').hide();
        $('.postFav').hide();
        // document.getElementById("#keyword").value="";
    }
    $('ul.nav-pills li a').click(function (e) 
    {
          $('ul.nav-pills li.active').removeClass('active');
          $(this).parent('li').addClass('active');
    });

     
});

