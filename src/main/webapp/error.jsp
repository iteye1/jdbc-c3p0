<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<HTML>

<HEAD>
	<TITLE>Test Page: Error</TITLE>
</HEAD>

<BODY BGCOLOR="#FFFFFF">

<H3>Error</H3>

There was an error: <c:out value="${DATA.Message}" />.

</BODY>
</HTML>
