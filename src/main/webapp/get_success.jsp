<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<HTML>

<HEAD>
	<TITLE>Test Page: Getting Data</TITLE>
</HEAD>

<BODY BGCOLOR="#FFFFFF">


<c:if test="${ empty DATA.Items }">

<H3>No Data Available</H3>

<c:url value="${initParam[ 'jdbc_pool.mapping.put_data' ]}" var="link" />

No data.  Why not <A HREF="${link}">add some data</A>, first?

</c:if>


<c:if test="${ ! empty DATA.Items }">

<H3>Data</H3>

	<UL>
	<c:forEach items="${DATA.Items}" var="item">

		<LI> <P><B>${item.name}</B> - ${item.number}</P>

	</c:forEach>
	</UL>

<c:url value="${initParam[ 'jdbc_pool.mapping.put_data' ]}" var="link" />

You can also <A HREF="${link}">add more data</A> if you'd like.

</c:if>


</BODY>
</HTML>
