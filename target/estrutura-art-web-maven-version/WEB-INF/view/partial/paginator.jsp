<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:if test="${paginator.isPaginator()}">
    <div class="row">
         <div class="col-sm-12">
             <div class="dataTables_paginate paging_simple_numbers" id="example_paginate">
                 <ul class="pagination justify-content-center">
                     <li class="paginate_button page-item previous ${paginator.getDisabled('previous')}" id="example_previous">
                         <a href="${paginator.getLink(paginator.getPrevious())}" aria-controls="example" data-dt-idx="0" tabindex="0" class="page-link">Anterior</a>
                     </li>

                    <c:forEach items="${paginator.getHref()}" var="href">
                        <c:choose>
                            <c:when test="${paginator.getPage() == href}">
                                 <li class="paginate_button page-item active">
                                       <a href="javascript:void(0);" class="page-link"><b>${href}</b></a>
                                     </li>
                             </c:when>
                             <c:otherwise>
                                   <li class="paginate_button page-item">
                                     <a href="${paginator.getLink(href)}" aria-controls="example" data-dt-idx="${href}" tabindex="${href}" class="page-link">${href}</a>
                                     </li>
                                 </c:otherwise>
                         </c:choose>
                    </c:forEach>

                    <li class="paginate_button page-item next ${paginator.getDisabled('last')}" id="example_next">
                        <a href="${paginator.getLink(paginator.getNext())}" aria-controls="example" data-dt-idx="4" tabindex="0" class="page-link">Próximo</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</c:if>