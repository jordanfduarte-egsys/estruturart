package br.com.estruturart.controller;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import br.com.estruturart.model.TbLancamento;
import br.com.estruturart.model.TbUsuario;
import br.com.estruturart.model.TbMaterial;
import br.com.estruturart.persistency.Lancamento;
import br.com.estruturart.persistency.Material;
import br.com.estruturart.utility.Exception1001;
import br.com.estruturart.utility.FlashMessenger;
import br.com.estruturart.utility.Paginator;
import br.com.estruturart.utility.Util;
import java.text.SimpleDateFormat;
import br.com.estruturart.utility.ParamRequestManager;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "lancamento", urlPatterns = { "/lancamento", "/lancamento/cadastro", "/lancamento/index/page/*", "/lancamento/editar/id/*" })
public class LancamentoController extends AbstractServlet
{
    private static final long serialVersionUID = -4214231788151587849L;

    public void indexAction() throws Exception {
        int page = Integer.parseInt(this.getParamOr("page", "1"));
        int offset = 10;
        System.out.println("PAGINA ATUAL: " + Integer.parseInt(this.getParamOr("page", "1")));
        Lancamento modelLancamento = new Lancamento();
        Paginator paginator = modelLancamento.findAllPaginated(page, offset, this.postFilter());

        paginator.setLink(getServletContext().getInitParameter("source").toString() + "lancamento/index/page/{id}");
        System.out.println("TOTAL LANCAMENTOS: " + paginator.getIterator().size());
        this.getRequest().setAttribute("paginator", paginator);
        this.getRequest().setAttribute("filter", this.postFilter());
    }

    public void cadastroAction() throws Exception
    {
        int fkLancamento = Integer.parseInt(this.getParamOr("id", "0"));

        Lancamento lancamentoModel = new Lancamento();
        TbLancamento lancamento = new TbLancamento();
        Material materialModel = new Material();

        List<TbMaterial> materiais = materialModel.findAll();
        try {
            if (fkLancamento != 0) {
                lancamento = lancamentoModel.getLancamentoById(fkLancamento);
                if (lancamento.getId() == 0) {
                    throw new Exception1001("Lançamento informado inválido!");
                }
            }

            if (this.getMethod().equals(HttpMethod.POST)) {
                lancamento.setMaterialId(Integer.parseInt(getParameterOrValue("material_id", "0")));
                lancamento.setPreco(Float.parseFloat(getParameterOrValue("preco", "0.00").replace(".", "").replace(",", ".")));
                lancamento.setDescricao(getParameterOrValue("descricao", ""));
                lancamento.setUsuarioId(((TbUsuario) getSession().getAttribute("usuario")).getId());

                if (lancamento.isValid()) {
                    if (lancamento.getMaterialId() > 0) {
                        lancamento.setPreco(lancamento.getPreco() * -1);
                    }

                    if (fkLancamento != 0) {
                        lancamentoModel.update(lancamento);
                    } else {
                        lancamentoModel.insert(lancamento);
                    }

                    getFlashMessenger().setType(FlashMessenger.SUCCESS).add("Lançamento salvo com sucesso!");
                    redirect("lancamento");
                }
            }
        } catch (Exception1001 ee) {
            getFlashMessenger().setType(FlashMessenger.ERROR).add(ee.getMessage());
            redirect("lancamento");
        } catch (Exception e) {
            getLogErrorService().createLog(e);
            getFlashMessenger().setType(FlashMessenger.ERROR).add("Ocorreu um erro ao salvar o lançamento!");
        }

        this.getRequest().setAttribute("lancamento", lancamento);
        this.getRequest().setAttribute("materiais", materiais);
    }

    public void editarAction() throws Exception
    {
        this.getRoute().setAction("cadastroAction");
        this.cadastroAction();
    }

    public void fluxoCaixaAction() throws Exception
    {
        String dataIni = Util.dataBrToEn(getParameterOrValue("data_ini", ""));
        String dataFim = Util.dataBrToEn(getParameterOrValue("data_fim", ""));

        if (dataIni.equals("") || dataFim.equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            dataIni = df.format(c.getTime());
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            dataFim = df.format(c.getTime());
        }

        Lancamento lancamentoModel = new Lancamento();
        List<TbLancamento> lancamentos = lancamentoModel
            .findFluxoCaixa(dataIni, dataFim);
        TbLancamento totais = lancamentoModel
            .findFluxoCaixaTotais(dataIni, dataFim);

        getSession().setAttribute("session_fluxo_caixa", this.postFilter());
        this.getRequest().setAttribute("lancamentos", lancamentos);
        this.getRequest().setAttribute("totais", totais);
        this.getRequest().setAttribute("dataIni", Util.dataEnToBr(dataIni));
        this.getRequest().setAttribute("dataFim", Util.dataEnToBr(dataFim));
    }

    public void fluxoCaixaImpressaoAction() throws Exception
    {
        String dataIni = "";
        String dataFim = "";

        if (getSession().getAttribute("session_fluxo_caixa") instanceof ParamRequestManager) {
            ParamRequestManager postFilter = (ParamRequestManager) getSession().getAttribute("session_fluxo_caixa");
            dataIni = Util.dataBrToEn(postFilter.getParam("data_ini"));
            dataFim = Util.dataBrToEn(postFilter.getParam("data_fim"));
        }

        if (dataIni.equals("") || dataFim.equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            dataIni = df.format(c.getTime());
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            dataFim = df.format(c.getTime());
        }

        Lancamento lancamentoModel = new Lancamento();
        List<TbLancamento> lancamentos = lancamentoModel
            .findFluxoCaixa(dataIni, dataFim);
        TbLancamento totais = lancamentoModel
            .findFluxoCaixaTotais(dataIni, dataFim);

        setNoRender(true);
        getRequest().setAttribute("lancamentos", lancamentos);
        getRequest().setAttribute("totais", totais);
        getRequest().setAttribute("dataIni", Util.dataEnToBr(dataIni));
        getRequest().setAttribute("dataFim", Util.dataEnToBr(dataFim));
        getRequest().setAttribute("source", getServletContext().getInitParameter("source"));
        getRequest().getRequestDispatcher("/WEB-INF/view/lancamento/fluxo-caixa-impressao.jsp")
            .forward(this.getRequest(), this.getResponse());
    }
}
