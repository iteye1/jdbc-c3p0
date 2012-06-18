<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<HTML>

<HEAD>
	<TITLE>Test Page: Putting Data</TITLE>
</HEAD>

<BODY BGCOLOR="#FFFFFF">

Successfully added data.

<c:url value="${initParam[ 'jdbc_pool.mapping.get_data' ]}" var="link" />

Now you can <A HREF="${link}">see the data</A>.

</BODY>
</HTML>
