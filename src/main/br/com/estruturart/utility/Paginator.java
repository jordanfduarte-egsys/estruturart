package br.com.estruturart.utility;

import java.util.ArrayList;
import java.util.List;

public class Paginator
{
    private List<?> objects;
    private int total;
    private int itensPerPage;
    private int page;
    private int to;
    private int from;
    private int next;
    private String link;

    public Paginator(List<?> objects, int total, int itensPerPage, int page)
    {
        this.objects = objects;
        this.total = total;
        this.itensPerPage = itensPerPage;
        this.page = page;

        this.itens();
    }

    public List<?> getIterator()
    {
        return this.objects;
    }

    private int arredondar (int total, int perPage)
    {
        int result = total / perPage;

        if (total % perPage != 0) {
            result = (total / perPage + 1);
        }

        return result;
    }

    private void itens()
    {
        int maxPage = 10; // total elementos
        int li = maxPage;
        int ate = maxPage -1;
        int divisao = this.arredondar(this.total, this.itensPerPage);





        if (divisao < maxPage) {
            ate = divisao - 1;
            li = divisao;
        }

        if (this.page > Math.floor(maxPage/2)) {
            if (this.page + Math.floor(maxPage/2) < divisao) {
                li = this.page + (int) Math.floor(maxPage/2);
            } else {
                  li = divisao;
            }
        }

        this.next = divisao;
        this.to = li;
        this.from = ate;
    }

    public int getTo()
    {
        return this.to;
    }

    public int getFrom()
    {
        return this.from;
    }

    public int getPage()
    {
        return this.page;
    }

    public boolean isPaginator()
    {
        return this.total > this.itensPerPage;
    }

    public int getPrevious()
    {
        int previous = 1;
        if (this.page-1 > 1) {
            previous = this.page - 1;
        }

        return previous;
    }

    public List<Integer> getHref()
    {
        List<Integer> hrefs = new ArrayList<Integer>();



        for (int i = this.to - this.from; i <= this.to; i++) {
            hrefs.add(i);

        }

        return hrefs;
    }

    public int getNext()
    {
        int next = this.next;
        if (this.page + 1 < this.next) {
            next = this.page + 1;
        }

        return next;
    }

    /**
     * @return the link
     */
    public String getLink(int page)
    {
        return link.replace("{id}", Integer.toString(page));
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link)
    {
        this.link = link;
    }

    public String getDisabled(String validOnLink)
    {
        if (validOnLink.equals("previous")) {
            return page != 1 ? "" : "disabled";
        } else {
            return page == this.getHref().size() ? "disabled" : "";
        }
    }
}
