<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<HTML>

<HEAD>
	<TITLE>Sample App: Start</TITLE>
</HEAD>

<BODY>

<H2>Sample App</H2>

<c:url value="${initParam[ 'jdbc_pool.mapping.get_data' ]}" var="getLink" />
<c:url value="${initParam[ 'jdbc_pool.mapping.put_data' ]}" var="putLink" />

<P>
<A HREF="${putLink}">put data</A>
</P>

<P>
<A HREF="${getLink}">get data</A>
</P>

</BODY>

</HTML>