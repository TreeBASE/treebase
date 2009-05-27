
<h1>Contents of file ${backing.filename}:</h1>

<h2>State: ${state}</h2>

<p>(${backing.filename}) ${backing.contents}</p>

<form method="post" id="simpleForm">

<input name="filename" value="${backing.filename }">

<input type="submit" name="Search" value="Read file" />
</form>
	
<script type="text/javascript">
function formSubmit(form) {
	form.submit();
}
</script>
