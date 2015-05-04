/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import GsServer.Utils;
import Utorrent.Torrent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Julio
 */
public class TorrentTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    /* Lista de Cliente que representam as linhas. */
    private List<Torrent> linhas;

    /* Array de Strings com o nome das colunas. */
    private String[] colunas = new String[]{
        "Nome", "Status", "Progresso", "Legenda"};

    /* Cria um TorrentTableModel vazio. */
    public TorrentTableModel() {
        linhas = new ArrayList<Torrent>();
    }

    /* Cria um TorrentTableModel carregado com 
     * a lista de Torrent especificada. */
    public TorrentTableModel(List<Torrent> listaDeTorrent) {
        linhas = new ArrayList<Torrent>(listaDeTorrent);
    }

    /* Retorna a quantidade de colunas. */
    @Override
    public int getColumnCount() {
        // EstÃ¡ retornando o tamanho do array "colunas".  
        return colunas.length;
    }

    /* Retorna a quantidade de linhas. */
    @Override
    public int getRowCount() {
        // Retorna o tamanho da lista de Torrent.  
        return linhas.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        // Retorna o conteÃºdo do Array que possui o nome das colunas  
        return colunas[columnIndex];
    }

    ;  
  
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    ;  
  
  
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Torrent torrent = linhas.get(rowIndex);

        // Retorna o campo referente a coluna especificada.  
        // Aqui é feito um switch para verificar qual é a coluna  
        // e retornar o campo adequado. As colunas sãoas mesmas  
        // que foram especificadas no array "colunas".  
        switch (columnIndex) {

            case 0:
                return torrent.getName();
            case 1: {
                String status = torrent.getStatusString();
                return status;
            }
            case 2:
                return torrent.getProgress();
            case 3: {
                if (Utils.isVideoTemLegenda(torrent)) {
                    return "Sim";
                } else {
                    return "Não";
                }
            }
            case 4: {
                return torrent.getProgress();

            }
            default:
                // Isto não deveria acontecer...  
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
    }

    @Override
//modifica na linha e coluna especificada  
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Torrent torrent = linhas.get(rowIndex); // Carrega o item da linha que deve ser modificado  

        switch (columnIndex) { // Seta o valor do campo respectivo  
            case 0:
                torrent.setName(aValue.toString());
                break;
            case 1:
                torrent.setStatus(Integer.valueOf(aValue.toString()));
                
                break;
            case 2:
                torrent.setProgress(Integer.valueOf(aValue.toString()));
                break;
            case 3: String b ="";
                    break;

            case 4:
                String a = "";
                break;

            default:
            // Isto não deveria acontecer...               
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    //modifica na linha especificada  
    public void setValueAt(Torrent aValue, int rowIndex) {
        Torrent torrent = linhas.get(rowIndex); // Carrega o item da linha que deve ser modificado  

        torrent.setName(aValue.getName());
        torrent.setStatus(aValue.getStatus());
        torrent.setProgress(aValue.getProgress());
        torrent.setFormatedSize(aValue.getSize());

        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
        fireTableCellUpdated(rowIndex, 2);
        fireTableCellUpdated(rowIndex, 3);
        fireTableCellUpdated(rowIndex, 4);

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Torrent getTorrent(int indiceLinha) {
        return linhas.get(indiceLinha);
    }

    /* Adiciona um registro. */
    public void addTorrent(Torrent m) {
        // Adiciona o registro.  
        linhas.add(m);

        int ultimoIndice = getRowCount() - 1;

        fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    /* Remove a linha especificada. */
    public void removeTorrent(int indiceLinha) {
        linhas.remove(indiceLinha);

        fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    /* Adiciona uma lista de Torrent ao final dos registros. */
    public void addListaDeTorrent(List<Torrent> torrent) {
        // Pega o tamanho antigo da tabela.  
        int tamanhoAntigo = getRowCount();

        // Adiciona os registros.  
        linhas.addAll(torrent);

        fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
    }

    /* Remove todos os registros. */
    public void limpar() {
        linhas.clear();

        fireTableDataChanged();
    }

    /* Verifica se este table model esta vazio. */
    public boolean isEmpty() {
        return linhas.isEmpty();
    }
}
