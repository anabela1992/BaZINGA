









<?php
include('config.php');

function toMySQL($date){
//Take our UK date string and parse it into a MySQL date YY-MM-DD HH:MM:SS
$day =  substr($date,9,2);
$month = substr($date,12,2);
$year = substr($date,15,4); 

$hour = substr($date,0,2); 
$min = substr($date,3,2);
$sec = substr($date,6,2); 

$retVal = $year."-".$month."-".$day." ".$hour.":".$min.":".$sec;
 //print $retVal."<br>";
return $retVal;
}


$now = date("H:i:s d/m/Y");
$thisMorning = date("00:00:00 d/m/Y");

$thisHour = date("H:00:00 d/m/Y");
$startLastHour = date("H:00:00 d/m/Y",strtotime(date("d-m-Y H:00:00")."- 1 hour"));
$endLastHour = date("H:59:59 d/m/Y",strtotime(date("d-m-Y H:00:00")."- 1 hour"));

$yesterdayMorning = date("00:00:00 d/m/Y", strtotime("-1 day", strtotime(date("d-m-Y 00:00:00"))));
$yesterdayEvening =date("H:i:s d/m/Y", strtotime("-1 day", strtotime(date("d-m-Y 23:59:59"))));

$startWeek = date("00:00:00 d/m/Y", mktime(1, 0, 0, date("m"), date("d")-date("w"), date("Y")));
$startMonth = date("00:00:00 01/m/Y");

$startLastMonth =date("00:00:00 01/m/Y", strtotime(date('d-m-Y')."- 1 month") ); 
$endLastMonth=date("23:59:59 d/m/Y", strtotime("-1 day", strtotime(date("m/01/Y"))));

$startLastWeek=date("00:00:00 d/m/Y",strtotime($date." - 1 week"));
$endLastWeek= date('d/m/Y 23:59:59',strtotime(date('d-m-Y 23:59:59', mktime(1, 0, 0, date('m'), date('d')-date('w'), date('Y')))."- 1 day"));


function secs($hours, $mins, $secs){

return (($hours*3600)+($mins*60)+$secs);
}
	$datapow = Array();
	$datatemp = Array();
	$datatime = Array();


	if( isset($_GET['type'])){
		//echo $_GET['type'];
		switch($_GET['type']) {
			case "TH":
				//echo $thisHour;
				$from = $thisHour;
				$to=	$now;		
			break;
			case "LH":
				$from =	$startLastHour;
                               	$to=	$endLastHour;
                        break;
			case "TD":
				$from =	$thisMorning;
                               	$to=	$now;
                        break;
			case "YD":
				$from =	$yesterdayMorning;
                               	$to=	$yesterdayEvening;
                        break;
			case "TW":
				$from =	$startWeek;
                               	$to=	$now;
                        break;
			case "LW":
				$from =	$startLastWeek;
                               	$to=	$startWeek;
                        break;
			case "TM":
				$from =	$startMonth;
                               	$to=	$now;
                        break;
			case "LM":
				$from =	$startLastMonth;
                               	$to=	$startMonth;
                        break;

		}
	
	}
        if( isset($_GET['to']))
                $to = $_GET['to'];

        elseif(!isset($_GET['type'])){
		$to = $now;
	}	
	
	if(isset($_GET['from']))
                $from = $_GET['from'];

        elseif(!isset($_GET['type'])){
                $from = $thisMorning;
	}

print "<title>From ".$from." to".$to."</title>";
print "<h1>".$from."...to...".$to."</h1>";

//print "<title>From ".date('H:i:s d/m/Y ',reset($datatime))." to".$to."</title>";
//print "<h1>".$from."...to...".date('H:i:s d/m/Y ',end($datatime))."</h1>";

if (!mysql_connect($db_host, $db_user, $db_pwd))    die("Can't connect 
to database");
if (!mysql_select_db($database))    die("Can't select database");

$query = "SELECT AVG(power), AVG(temp), SUM(joules) FROM consumption 
WHERE time <='".toMySQL($to)."'AND time >='".toMySQL($from)."' AND 
joules > 0 ORDER BY id ASC";
//print $query."<br>";
$result = mysql_query($query);

while($row = mysql_fetch_array($result)) {
        $avgPow = $row['AVG(power)'];
	$avgTemp = $row['AVG(temp)'];
	$joules = $row['SUM(joules)'];
}
//print $joules;

$query = "SELECT * FROM 
consumption 
WHERE time 
<='".toMySQL($to)."' AND time >= '".toMySQL($from)."' ORDER BY id ASC";

$result = mysql_query($query);

$records = mysql_num_rows($result);
$skip = ceil($records / $maxResults);

if($skip == 0)
	$skip =1;

$counter = 0;
$maxY = 0;
$index=0;

while ($row = mysql_fetch_array($result)) {
	$curPower = $row[power];
        if($curPower > $maxY){
                        $maxY = $curPower;
                        $maxYTime=strtotime($row[time]);
                }
	if($counter % $skip ==0){
		 
		$flotr[$index][0]= strtotime($row[time]);
		$flotr[$index][1]= floatval($curPower);
		
		$temp = $row[temp];
		$time = $row[time];
		$power = $curPower;
		$datatime[] = strtotime($row[time]);
	$index++;
	}
	$counter++;
}

$query = "SELECT power, temp FROM consumption ORDER BY id DESC LIMIT 1";
$result = mysql_query($query);
$row = mysql_fetch_array($result);

$temp = $row[temp];
$curPower = $row[power];
































	
	print"<table border=1>";
	print "<tr>";
	print "<td><b>Current Power:</b><td>".$curPower."<b>W</b></td></tr><tr>";
	print "<td><b>Current Temp:</b><td>".$temp."<b>C</b></td></tr></table>";
	
	print "<br><table border=1><tr>";
	print "<td><b>Readings From:</b><td>".date('H:i:s d/m/Y ',reset($datatime))."</td></tr><tr>";
	print "<td><b>To:</b><td>".date('H:i:s d/m/Y ',end($datatime))."</td></tr><tr>";
	print "<td><b>Avg Power:</b><td>".round($avgPow,1)."<b>W</b></td></tr><tr>";
        print "<td><b>Avg Temp:</b><td>".round($avgTemp,1)."<b>C</b></td></tr><tr>";
	print "<td><b>kWh:</b><td>".round(($joules/3600000),3)."<b>kWh</b></td></tr><tr>";
	#print "<td><b>Cost:</b><td>#".round( ($joules/360000000)*14.62,2)."</td></tr><tr>";

	if($maxY < 1000)
		print "<td><b>Max Power:</b><td>".$maxY."<b>W</b></td></tr><tr>";

	else
		print "<td><b>Max Power:</b><td>".($maxY/1000)."<b>kW</b></td></tr><tr>";

	print "<td><b>@:</b><td>".date('H:i:s d/m/Y',$maxYTime)."</td></tr><tr>";
	

	print"<script>var d1 = ".json_encode($flotr).";</script>";
	print"<script>document.getElementById('fromTxt').value ='".$from."';</script>";
	print"<script>document.getElementById('toTxt').value ='".$to."';</script>";

?>
