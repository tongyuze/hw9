<?php
	date_default_timezone_set('America/Los_Angeles');
	require_once __DIR__ . '/php-graph-sdk-5.0.0/src/Facebook/autoload.php';
	$fb = new Facebook\Facebook([
  		'app_id' => '986969244770042',
  		'app_secret' => '8f3cbe708be0deae76cd1ad102d9f55d',
  		'default_graph_version' => 'v2.8',
  	]);
  	$_myAccessToken
  	='EAAOBpK89HvoBAIY8wyFPcsU9DDR4fWGysq5k54gM2veJ34nw7n7ZAIgg0YdmUmQTUA1IU871HsgWzzs4LfC6RR0W2CvaTzIHCvrGgtVC0ZARxZCCpc9zrrQM5NjelIZCXflIiH0wZC26cv1LGaesE2LNWOJEzDtcZD';
  	$_url = 'search?q=';
  	if(isset($_GET["keyword"]))
  	{
		$keyword = $_GET["keyword"];
		if(!isset($_GET["lat"])&&(!isset($_GET["long"])))
		{	
			$lat = 0;
			$long = 0;
		}
		else
		{
			$lat = $_GET["lat"];
			$long = $_GET["long"];
		}
		$link1 = $_url.urlencode($keyword).'&'.'type=user&fields=id,name,picture.width(700).height(700)&limit=10&access_token='.$_myAccessToken;
		$link2 = $_url.urlencode($keyword).'&'.'type=page&fields=id,name,picture.width(700).height(700)&limit=10&access_token='.$_myAccessToken;
		$link3 = $_url.urlencode($keyword).'&'.'type=event&fields=id,name,picture.width(700).height(700)&limit=10&access_token='.$_myAccessToken;
		$link4 = $_url.$keyword.'&'.'type=place&center='.$lat.','.$long.'&fields=id,name,picture.width(700).height(700)&limit=10&access_token='.$_myAccessToken;
		$link5 = $_url.urlencode($keyword).'&'.'type=group&fields=id,name,picture.width(700).height(700)&limit=10&access_token='.$_myAccessToken;
		$fbres1 = $fb->get($link1);
		$fbjson1 = $fbres1->getBody();
		$fbres2 = $fb->get($link2);
		$fbjson2 = $fbres2->getBody();
		$fbres3 = $fb->get($link3);
		$fbjson3 = $fbres3->getBody();
		$fbres4 = $fb->get($link4);
		$fbjson4 = $fbres4->getBody();
		$fbres5 = $fb->get($link5);
		$fbjson5 = $fbres5->getBody();
		$array = array(json_decode($fbjson1,true),json_decode($fbjson2,true),json_decode($fbjson3,true),json_decode($fbjson4,true),json_decode($fbjson5,true));
		$fbjson = json_encode($array);
		echo $fbjson;
	}
	if(isset($_GET["id"]))
	{
		$id = $_GET["id"];
		$urll = $id.'?fields=id,name,picture.width(700).height(700),albums.limit(5){name,photos.limit(2){name, picture}},posts.limit(5)&access_token='.$_myAccessToken;
    	$getres = $fb->get($urll);
    	$detail = $getres->getGraphNode();
    	if(isset($detail['albums']))
    	{
    		$details = $detail['albums'];
	    	foreach($details as $picname)
			{
				$photos = $picname['photos'];
				foreach($photos as $pho)
		        {
					$picres = $fb->get($pho['id'].'/picture?'.'access_token='.$_myAccessToken);
					$hires = $picres->getHeaders();
					$purl = $hires['Location'];	
					$pho['picture'] = $purl;
				}
				$picname['photos'] = $photos;
			}
			$detail['albums'] = $details;
		}	
    	echo $detail;
	}
?>
