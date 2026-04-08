<%@ tag description="Help button component" pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="topic" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="false" rtexprvalue="true" %>
<%@ attribute name="tooltip" required="false" rtexprvalue="true" %>

<a href="#" class="openHelp"
   onclick="openHelp('${topic}')"
   ${not empty tooltip ? 'data-bs-toggle="tooltip" data-bs-placement="top" title="'.concat(tooltip).concat('"') : ''}>
    <i class="fa fa-question-circle"></i>${empty label ? '' : ' '.concat(label)}
</a>