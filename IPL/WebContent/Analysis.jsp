<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IPL Analysis</title>
<script type="text/javascript">
	
var request;  
function sendInfo()  
{  
var progress="<div class='progress'><div class='indeterminate'></div></div>";
document.getElementById('bizz').innerHTML=progress;
var v=document.vinform.choice.value;  
var url="ServerIPL?val="+v;  
  
if(window.XMLHttpRequest){  
request=new XMLHttpRequest();  
}  
else if(window.ActiveXObject){  
request=new ActiveXObject("Microsoft.XMLHTTP");  
}  
  
try  
{  
request.onreadystatechange=getInfo;  
request.open("GET",url,true);  
request.send();  
}  
catch(e)  
{  
alert("Unable to connect to server");  
}  
}  
  
function getInfo(){  
if(request.readyState==4){  
var val=request.responseText; 
var i,j;
var table="<table id='dataTable' cellpadding='5'>";
var rows=val.split("<br>");
var col = rows[0].split(":");
table+="<tr style='background-color:orange;color:white'><td style='border:1px solid white'>Number</td>";
for(i=0;i<col.length;i++){
	table+="<td style='border:1px solid white'>"+col[i]+"</td>";
}
table+="</tr>";
var count=0;
for(i=1;i<rows.length-1;i++){
	count++;
	var cols=rows[i].split(":");
	if (i%2==0)
		table+="<tr style='background-color:#dfdcdb'>";
	else 
		table+="<tr>";
	table+="<td style='border:1px solid orange'>"+count+"</td>";
	for(j=0;j<cols.length;j++){
		table+="<td style='border:1px solid orange'>"+cols[j]+"</td>";
	}
	table+="</tr>"
}
table+="</table>";
document.getElementById('bizz').innerHTML=table;
}  
}  
  
</script>  
<style>
.progress {
  position: relative;
  height: 4px;
  display: block;
  width: 100%;
  background-color: #f44006;
  border-radius: 2px;
  background-clip: padding-box;
  margin: 0.5rem 0 1rem 0;
  overflow: hidden; }
  .progress .determinate {
    position: absolute;
    background-color: inherit;
    top: 0;
    bottom: 0;
    background-color: orange;
    transition: width .3s linear; }
  .progress .indeterminate {
    background-color: orange; }
    .progress .indeterminate:before {
      content: '';
      position: absolute;
      background-color: inherit;
      top: 0;
      left: 0;
      bottom: 0;
      will-change: left, right;
      -webkit-animation: indeterminate 2.1s cubic-bezier(0.65, 0.815, 0.735, 0.395) infinite;
              animation: indeterminate 2.1s cubic-bezier(0.65, 0.815, 0.735, 0.395) infinite; }
    .progress .indeterminate:after {
      content: '';
      position: absolute;
      background-color: inherit;
      top: 0;
      left: 0;
      bottom: 0;
      will-change: left, right;
      -webkit-animation: indeterminate-short 2.1s cubic-bezier(0.165, 0.84, 0.44, 1) infinite;
              animation: indeterminate-short 2.1s cubic-bezier(0.165, 0.84, 0.44, 1) infinite;
      -webkit-animation-delay: 1.15s;
              animation-delay: 1.15s; }

@-webkit-keyframes indeterminate {
  0% {
    left: -35%;
    right: 100%; }
  60% {
    left: 100%;
    right: -90%; }
  100% {
    left: 100%;
    right: -90%; } }
@keyframes indeterminate {
  0% {
    left: -35%;
    right: 100%; }
  60% {
    left: 100%;
    right: -90%; }
  100% {
    left: 100%;
    right: -90%; } }
@-webkit-keyframes indeterminate-short {
  0% {
    left: -200%;
    right: 100%; }
  60% {
    left: 107%;
    right: -8%; }
  100% {
    left: 107%;
    right: -8%; } }
@keyframes indeterminate-short {
  0% {
    left: -200%;
    right: 100%; }
  60% {
    left: 107%;
    right: -8%; }
  100% {
    left: 107%;
    right: -8%; } }
#dataTable{
	border:2px solid orange;
	border-collapse:collapse;
	font-size:16px;
}
#findButton {
	background-color: orange;
	color: white;
	size: 30;
	padding-left:40px;
	padding-right:40px;
	padding-top:10px;
	padding-bottom:12px;
	border-style:none;
	border:1px solid white;
	border-radius:5px;
	font-size:30px;
}
#findButton:hover{
	background-color: #f44006;
	border:1px solid #f44006;
	color:white;
}
#choice{
	background-color:white;
	border-style:solid;
	border-width:2px;
	border-color:orange;
	padding-top:10px;
	padding-bottom:10px;
	padding-left:5px;
	padding-right:5px;
	border-radius:5px;
	font-family:"Times New Roman";
	font-size:26px;
	color:orange;
	-webkit-appearance: none; 
   -moz-appearance: none;
}
#choice:hover {
	color:#f44006;
	border-color:#f44006;
}
</style>
</head>
<!--End of Head-->
<body>
<form name="vinform">
<table style="width:100%;margin:0 auto;text-align:center">
<tr>
<td><select name="choice" id="choice">
<option>Batsman Total Runs</option>
<option>Batsman Highest Runs</option>
<option>Bowler Extras</option>
<option>Bowler Wickets</option>
<option>Best Bowling Figures</option>
</select>
<input type="button" value="Find" id="findButton" onclick="sendInfo()"/></td>
</tr>
</table>
</form>
<br><br>
<div id="bizz" align="center" style="width:100%"></div>
</body>
</html>