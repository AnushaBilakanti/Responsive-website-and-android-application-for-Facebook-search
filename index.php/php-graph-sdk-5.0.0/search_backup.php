<!DOCTYPE html>
<?php require_once __DIR__ . '/php-graph-sdk-5.0.0/src/Facebook/autoload.php';?>
<html>
    <head>
        <meta charset="UTF-8">
        <style>
            .box
            {
                border: 5px solid gainsboro;
                width: 600px;
                height: 180px;
                margin: auto;
                text-align: left;
                padding-left: 10px;
                padding-right: 10px;
                background-color: gainsboro;
            }
            #fb
            {
                 text-align: center;
                font-size: 20px;
                font-weight: bold;
            }
            #type_id
            {
                margin-left: 34px;
                margin-bottom:2px;
                margin-top: 2px;

            }
            #key
            {
                margin-left: 10px;

            }
            #submit
            {
                margin-left: 70px;

            }
            #clear
            {
                margin-left: 20px;

            }
            #location
            {
                margin-left: 13px;

            }
            #spl
            {
                text-align: left;
            }
            #result
            {
                display:table; margin-left: auto; margin-right: auto; width: 800px;
                margin-top: 10px;
                text-align: center;
            }
            table,th,td {text-align: center; border: 5px solid gainsboro; border-collapse: collapse; margin-left: auto; margin-right: auto;}
            .zero {text-align: center} /*display */
        </style>
    </head>
    <body>
        <script type="text/javascript">

            function checktype()
            {
                var element=document.getElementById('type_id').value;
                if(element=="user")
                    {
                        document.getElementById('loc_dis').style.display = 'none';
                    }
               else if(element=="page")
                    {

                         document.getElementById('loc_dis').style.display = 'none';
                    }
                else if(element=="event")
                    {
                         document.getElementById('loc_dis').style.display = 'none';
                    }
                else if(element=="place")
                    {
                        document.getElementById('loc_dis').style.display = 'block';

                    }
                else if(element=="group")
                    {
                        document.getElementById('loc_dis').style.display = 'none';
                    }
            }
            function clearform()
            {
                document.getElementById("loc_dis").style.display = "none";
                document.getElementById("key").value = "";
                document.getElementById("type_id").value = "user";
                document.getElementById("location").value= "";
                document.getElementById("distance").value = "";
            }
            function toggle_visibility(divID, otherDivId) //divID-albumlist;otherDivId-postlist
            {
                var item1 = document.getElementById(divID);
                var item2 = document.getElementById(otherDivId);
                if(item1.style.display == 'none')
                {
                    item1.style.display = 'block';
                    item2.style.display = 'none';

                }
                else if(item1.style.display == 'block')
                {
                    item1.style.display = 'none';
//                    item2.style.display = 'none';
                }
                else if(item2.style.display == 'none')
                {
                   item2.style.display = 'block';
                    item1.style.display = 'none';
                }
                else if(item2.style.display == 'block')
                {
                    item2.style.display = 'none';
//                    item2.style.display = 'none';
                }
            }
            function change_attr(picid)
            {
                var item=document.getElementById(picid);
                if(item.style.display == 'none')
                {
                    item.style.display = 'block';
                }
                else if(item.style.display == 'block')
                {
                    item.style.display = 'none';
                }
            }
        </script>
        <form method="POST" id="myForm" name="myForm" action="search_backup.php">
            <div class="box">

                <div id="fb"><i>Facebook Search</i></div>
                <hr>
                Keyword<input type="text" id="key" name="keyword" required value="<?php
                    if(isset($_POST['keyword'])) {
                        echo htmlentities($_POST['keyword']);  // always filter outputs of external data
                        }
                    if(isset($_GET['keyword'])) {
                        echo htmlentities($_GET['keyword']);  // always filter outputs of external data
                        }
                    ?>" />
                <br>
                Type <select id="type_id" required selected="Users" name="type" onchange="checktype();" value="<?php echo $_POST['type'];?>">
                <option value="<?php echo (isset($_POST['type']) && $_POST['type'] == 'user')?'selected="selected"':''; php echo (isset($_GET['type']) && $_GET['type'] == 'user')?'selected="selected"':'';?>">Users</option>
                <option value="<?php echo (isset($_POST['type']) && $_POST['type'] == 'page')?'selected="selected"':''; php echo (isset($_GET['type']) && $_GET['type'] == 'page')?'selected="selected"':'';?>">Pages</option>
                <option value="<?php echo (isset($_POST['type']) && $_POST['type'] == 'event')?'selected="selected"':''; php echo (isset($_GET['type']) && $_GET['type'] == 'event')?'selected="selected"':'';?>">Events</option>
                <option value="<?php echo (isset($_POST['type']) && $_POST['type'] == 'place')?'selected="selected"':''; php echo (isset($_GET['type']) && $_GET['type'] == 'place')?'selected="selected"':'';?>">Places</option>
                <option value="<?php echo (isset($_POST['type']) && $_POST['type'] == 'group')?'selected="selected"':''; php echo (isset($_GET['type']) && $_GET['type'] == 'group')?'selected="selected"':'';?>">Groups</option>
                </select>
                <br>
                <div style="display: none;" id="loc_dis">
                    Location<input type="text" id="location" name="loc_addr">
                    Distance(meters)<input type="text" id="distance" name="dist_addr">
                </div>
                <br>
                <br>
                <input type="submit" id="submit" value="Search" name="submit">
                <input type="button" id="clear1" onclick="clearform()" value="Clear">
                <br>
            </div>
        </form>
            <div id="result" >
                <?php
                if($_SERVER["REQUEST_METHOD"] == "POST")
                {
                        if(isset($_POST['submit']))
                        {
                                $keyword = rawurlencode($_POST['keyword']);

                                $type = $_POST['type'];
                                $enkeyword = urlencode($keyword);

                                $entype = urlencode($type);
                                //echo $enkeyword;
                                //echo $enType;

                                $fb = new Facebook\Facebook(['app_id' => '672782492904016','app_secret' => 'e77486d170e2b9e370826cf6640e0daa','default_graph_version' => 'v2.8']);
    $fb->setDefaultAccessToken('EAAJj5GGAKlABABFQlaK8TaUJE0m8XrT0hGDUmAdfQnzgadsAx6Qz3n8VSVHr4M9FtlS2TWgftWIDYbZB0XEIEZCSgxuBJDI1bNT4CpV9OF5G2PBWdo5TzugaLiFozekFKRqTsg1EHUAEhFe6qpjxyOas4Afk4ZD');

                                if($entype =="user"||$entype =="group"||$entype =="page")
                                {
                    $fbURL = "/search?q=".$keyword."&type=".$entype."&fields=id,name,picture.width(700).height(700)";
                                }
                                else if($entype =="event")
                                {
            $fbURL = "/search?q=".$keyword."&type=".$entype."&fields=id,name,picture.width(700).height(700),place";
                                }
                                else if($entype =="place")
                                {
                                    $location=$_POST['loc_addr'];
                                    $location = urlencode($location);
                                    $distance=$_POST['dist_addr'];
                                    
                                    if($location!=NULL && $distance!=NULL)
                                    {
                                        echo "HELLO".$location.$distance;
                                        $url_addr = "https://maps.googleapis.com/maps/api/geocode/json?address=".$location."&key=AIzaSyBh8gzURU1CnUwL1nOeDtq86VcaIQ_inB4";
                                        $url_addr=urlencode($url_addr);

                                        $addr_json = file_get_contents(urldecode($url_addr));
                                        $addr=json_decode($addr_json, true);

                                        $size_addr=sizeof($addr['results']);
                                        if($size_addr==0)
                                        {
                                            echo '<html><body><h3>No Records have been found</h3></body></html>';
                                        }
                                        else
                                        {
                                            $lati = $addr['results'][0]['geometry']['location']['lat'];
                                            $longi = $addr['results'][0]['geometry']['location']['lng'];

                                            $fbURL = "/search?q=".$keyword."&type=".$entype."&center=".$lati.",".$longi."&distance=".$distance."&fields=id,name,picture.width(700).height(700)";

                                        }
                                        
                                    }
                                    else
                                    {
                                        $fbURL = "/search?q=".$keyword."&type=".$entype."&fields=id,name,picture.width(700).height(700)";
                                        
                                    } 
                    
                                }


//                                $sample="/search?q=usc&type=user&fields=id,name,picture.width(700).height(700)";
                                try
                                {
                                    $response = $fb->get($fbURL);
//                                    echo "https://graph.facebook.com/v2.8".$fbURL.;
                                    $get_data = $response->getDecodedBody();
                                    $count = sizeof($get_data['data']);

                                    if($count == 0)
                                    {
                                        echo "<p class = 'zero'>No Records have been found</p>";
                                    }
                                    else
                                    {
                                        if($entype =="event")
                                        {
                                           echo "<p><div id='display'><table style='background-color: #f2f2f2;'><tr><th>Profile Photo</th><th>Name</th><th>Place</th></tr>";
                                        }
                                        else
                                        {
                                            echo "<p><div id='display'><table style=background-color: #f2f2f2;'><tr><th>Profile Photo</th><th>Name</th><th>Details</th></tr>";

                                        }

                                        foreach ($get_data["data"] as $item)
                                        {
                                            $image = $item["picture"]["data"]["url"];
                                            $name = $item["name"];
                                            $id = $item["id"]; //will be required for details
                                            $place=$item["place"]["name"];
                                            echo "<tr><td><img onclick='window.open(this.src)' src=\"".$image."\"style='width:30px;height:40px;'</img></a></td>";
                                            echo "<td>".$name."</td>";
                                            if($entype =="event")
                                            {
                                               echo "<td>".$place."</td></tr>";
                                            }
                                            else
                                            {
                                                if(isset($_POST['loc_addr'])&& isset($_POST['dist_addr']))
                                                {
                                                    echo "<td><a href='search_backup.php?id=$id&keyword=$keyword&type=$type&location=$location&distance=$distance'>Details</a></td></tr>";
                                                }
                                                else
                                                {
                                                   echo "<td><a href='search_backup.php?id=$id&keyword=$keyword&type=$type'>Details</a></td></tr>"; 
                                                }
                                                
                                            }

                                        }
                                        echo "</table></div></p>";
                                    }
                                }
                                catch(Facebook\Exceptions\FacebookResponseException $e)
                                {
                                      echo 'Graph returned an error: ' . $e->getMessage();
                                      exit;
                                }
                                catch(Facebook\Exceptions\FacebookSDKException $e)
                                {
                                      echo 'Facebook SDK returned an error: ' . $e->getMessage();
                                      exit;
                                }
                        }//if(isset($_POST["submit"]))
                }//if($_SERVER["REQUEST_METHOD"] == "POST")
                if(isset($_GET['id']))
                {
                    $id = $_GET['id'];

                    $fb = new Facebook\Facebook(['app_id' => '672782492904016','app_secret' => 'e77486d170e2b9e370826cf6640e0daa','default_graph_version' => 'v2.8']);
    $fb->setDefaultAccessToken('EAAJj5GGAKlABABFQlaK8TaUJE0m8XrT0hGDUmAdfQnzgadsAx6Qz3n8VSVHr4M9FtlS2TWgftWIDYbZB0XEIEZCSgxuBJDI1bNT4CpV9OF5G2PBWdo5TzugaLiFozekFKRqTsg1EHUAEhFe6qpjxyOas4Afk4ZD');
                    $fbURL = $id."?fields=id,name,picture.width(700).height(700),albums.limit(5){name,photos.limit(2){name,picture}},posts.limit(5)";

                    try
                    {
                            $response = $fb->get($fbURL);
                            $get_data = $response->getDecodedBody();
                        
                            if(isset($get_data['albums'])==false)
                            {
                                echo "<div style='width:800px;background-color: gainsboro;height:20px'>No Albums have been found</div>";
                            }
                            else
                            {
                                echo "<div style='width:800px;background-color: gainsboro;height:20px'><a href=javascript:toggle_visibility('album_div','post_div')>Albums</a></div>";

                                echo "<div id='album_div' style='width:800px;display:none;margin-top:10px;'>";
                                echo "<table id=album_table border=1 style='margin-top:10px;width:800px;'>";
                                
                                $counter=0;
                                
                                foreach($get_data['albums']['data'] as $det)
                                {
                                    $counter=$counter+1;
                                    
                                    if(isset($det['photos']))
                                    {
                                        echo "<tr><td class=td4 style='text-align:left'><a href=javascript:change_attr('pics".$counter."')>".$det['name']."</a></td></tr>";
                                        
                                        echo "<tr id=pics".$counter." style='display:none;' class=td4 border=0.5>";
                                        echo "<td style='text-align:left'>";
                                        foreach($det['photos']['data'] as $pictures)
                                        {
//                                            echo "<td id=pics".$counter."hidden='true'>";
                                            
                                            $photo_id=$pictures['id'];
                                            $request_pic=$fb->request('GET','/'.$photo_id.'/picture');
                                            $response_pic=$fb->getClient()->sendRequest($request_pic);
                                            $result_pic=$response_pic->getHeaders();
//                                            echo "<a href=".$result_pic['Location']."><img style='width:70px;height:70px;' src=\"".$pictures['picture']."\" onclick='window.open(this.src)'</img></a>";
//                                            echo $pictures['picture'];
//                                            echo "<br/>";
echo "<a href='https://graph.facebook.com/v2.8/".$photo_id."/picture?access_token=EAAJj5GGAKlABABFQlaK8TaUJE0m8XrT0hGDUmAdfQnzgadsAx6Qz3n8VSVHr4M9FtlS2TWgftWIDYbZB0XEIEZCSgxuBJDI1bNT4CpV9OF5G2PBWdo5TzugaLiFozekFKRqTsg1EHUAEhFe6qpjxyOas4Afk4ZD' target='_blank'><img style='width:70px;height:70px;' src=\"".$pictures['picture']."\"/></a>";
                                        }
                                        echo "</td>";
                                        echo "</tr>";
                                         
                                    }
                                }
                                echo "</table></div>";
                              
                            }
                            if(isset($get_data['posts'])==false)
                            {
                                echo "<div style='width:800px;background-color: gainsboro;height:20px;margin-top:10px;'>No Posts have been found</div>";
                            }
                            else
                            {
                                 echo "<div style='width:800px;background-color: gainsboro;height:20px;margin-top:10px;'><a href=javascript:toggle_visibility('post_div','album_div')>Posts</a></div>";
                                
                                 echo "<div id='post_div' style='width:800px;display:none;margin-top:10px;'>";
                                
                                echo "<table id=album_table border=1 style='margin-top:10px;width:800px;'>";
                                echo "<tr><th style='text-align:left'>Message</th></tr>;";
                                foreach($get_data['posts']['data'] as $det)
                                { 
                                    echo "<tr><td>".$det['message']."</td></tr>";
                                }
                                echo "</table>";    
                            }
                    }
                    catch(Facebook\Exceptions\FacebookResponseException $e)
                    {
                          echo 'Graph returned an error: ' . $e->getMessage();
                          exit;
                    }
                    catch(Facebook\Exceptions\FacebookSDKException $e)
                    {
                          echo 'Facebook SDK returned an error: ' . $e->getMessage();
                          exit;
                    }

                }//if(isset($_POST['id']))

                

                ?>
            </div>

    </body>
</html>