<%@ page contentType="text/html; charset=UTF-8" %>
<div class="content p-4">
    <h2 class="mb-4">Error :(</h2>

    <div class="card mb-4">
        <div class="card-body">
            Ocorreu um erro ao realizar essa ação.
            <br/>
            <div>
                <ul>
                    <li>
                        ${e.getMessage()}
                    </li>
                    <li>
                        ${printStackTrace}
                    </li>
                    <li>
                        ${e.getLocalizedMessage()}
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>