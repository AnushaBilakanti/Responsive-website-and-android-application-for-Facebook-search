<?php
    require_once __DIR__ . '/php-graph-sdk-5.0.0/src/Facebook/autoload.php';
    $fb = new Facebook\Facebook(['app_id' => '282689898841018','app_secret' => 'bce1924e5b415f993889c27edf2f99d2','default_graph_version' => 'v2.8']); 
    
    
    // echo "HELLO";
    $section=$_GET['section'];

    // $fbURL = "/search?q=".$keyword."&type=".$type."&fields=id,name,picture.width(700).height(700)";
    
    if($section=='disp')
    {
        $keyword=$_GET['name'];
        $type=$_GET['type'];
        $keyword = urlencode($keyword);
        $type = urlencode($type);

        if($type=='place')
        {
            $latitude=$_GET['latitude'];
            $longitude=$_GET['longitude'];
            $fbURL = "https://graph.facebook.com/v2.8/search?q=".$keyword."&type=".$type."&fields=id,name,picture.width(700).height(700)&center=".$latitude.",".$longitude."&access_token=EAAEBGt8Ag7oBACF0bLZArU2yOENV2aCrRt2MazedkBVzgTkbfltsZAeYfJDNgG0yqyvqUFQI5mCY8zxzFYbrtiZA9JZAFDJsdKQpFpBjn9ZBZC9G5x9PW0kB7LV2OOMwzNyo4invhkT1hwsrl7vVP8aDVt3eLJZCHkZD";
        }
        else
        {
            $fbURL = "https://graph.facebook.com/v2.8/search?q=".$keyword."&type=".$type."&fields=id,name,picture.width(700).height(700)&access_token=EAAEBGt8Ag7oBACF0bLZArU2yOENV2aCrRt2MazedkBVzgTkbfltsZAeYfJDNgG0yqyvqUFQI5mCY8zxzFYbrtiZA9JZAFDJsdKQpFpBjn9ZBZC9G5x9PW0kB7LV2OOMwzNyo4invhkT1hwsrl7vVP8aDVt3eLJZCHkZD"; 
        }

        
    }
    if($section=='albumpost')
    {
        $id=$_GET['id'];
        $fbURL="https://graph.facebook.com/v2.8/".$id."?fields=albums.limit(5){name,photos.limit(2){name,picture,images}},posts.limit(5){created_time,message}&access_token=EAAEBGt8Ag7oBACF0bLZArU2yOENV2aCrRt2MazedkBVzgTkbfltsZAeYfJDNgG0yqyvqUFQI5mCY8zxzFYbrtiZA9JZAFDJsdKQpFpBjn9ZBZC9G5x9PW0kB7LV2OOMwzNyo4invhkT1hwsrl7vVP8aDVt3eLJZCHkZD";

    }
    if(isset($_GET['url_id']))
    {
        $url=$_GET['url_id'];  
        $fbURL=$url;
    }
    // $json = file_get_contents($fbURL);
    // $result = json_decode($json, true); 
    // $response = $fb->get($fbURL);
    // // //                                    echo "https://graph.facebook.com/v2.8".$fbURL.;
    // $get_data = json_decode($response->getBody(),true);
    // echo "hi".$fbURL;
    $get_data = file_get_contents($fbURL,false);
    // $temp=array();
    // $count=1;
    // foreach ($get_data["data"] as $item) {
    //     $id=$count;
    //     $count=$count+1;
    //     $temp[$id]=array("id"=>$item["id"],"name"=>$item["name"],"url"=>$item["picture"]["data"]["url"]);
    // }
    echo $get_data;
    
    // echo json_encode(array("response"=>$get_data));

?>
   