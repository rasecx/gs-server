/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tables;

import GsServer.Utils;
import Utorrent.TorrentFile;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Julio
 */
public class TorrentFileTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    /* Lista de Cliente que representam as linhas. */
    private List<TorrentFile> linhas;

    /* Array de Strings com o nome das colunas. */
    private String[] colunas = new String[]{
        "Patch", "Tamanho", "Done", "Priority"};

    /* Cria um TorrentFileTableModel vazio. */
    public TorrentFileTableModel() {
        linhas = new ArrayList<TorrentFile>();
    }

    /* Cria um TorrentFileTableModel carregado com 
     * a lista de TorrentFile especificada. */
    public TorrentFileTableModel(List<TorrentFile> listaDeTorrentFile) {
        linhas = new ArrayList<TorrentFile>(listaDeTorrentFile);
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
        // Retorna o tamanho da lista de TorrentFile.  
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
        TorrentFile file = linhas.get(rowIndex);

        // Retorna o campo referente a coluna especificada.  
        // Aqui é feito um switch para verificar qual é a coluna  
        // e retornar o campo adequado. As colunas sãoas mesmas  
        // que foram especificadas no array "colunas".  
        switch (columnIndex) {

            // Seguindo o exemplo: "Tipo","Data de Cadastro", "Nome", "Idade"};  
            case 0:
                return file.getFileName();
            case 1:
                return Utils.converterBytes(file.getFileSize());
            case 2: 
                return Utils.converterBytes(file.getDownloaded());
            case 3:
                return file.getPriorityString();
            default:
                // Isto não deveria acontecer...  
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
    }

    @Override
//modifica na linha e coluna especificada  
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        TorrentFile file = linhas.get(rowIndex); // Carrega o item da linha que deve ser modificado  

        switch (columnIndex) { // Seta o valor do campo respectivo  
             case 0:
                 file.getFileName();
            case 1:
                 file.getFileSize();
            case 2: 
                 file.getDownloaded();
            case 3:
                 file.getPriorityString();

            default:
            // Isto não deveria acontecer...               
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    //modifica na linha especificada  
    public void setValueAt(TorrentFile aValue, int rowIndex) {
        TorrentFile file = linhas.get(rowIndex); // Carrega o item da linha que deve ser modificado  

//        file.set (aValue.getFileName());
//        file.setStatus(aValue.getStatus());
//        file.setProgress(aValue.getProgress());

        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
        fireTableCellUpdated(rowIndex, 2);
        fireTableCellUpdated(rowIndex, 3);

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public TorrentFile getTorrentFile(int indiceLinha) {
        return linhas.get(indiceLinha);
    }

    /* Adiciona um registro. */
    public void addTorrentFile(TorrentFile m) {
        // Adiciona o registro.  
        linhas.add(m);

        int ultimoIndice = getRowCount() - 1;

        fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    /* Remove a linha especificada. */
    public void removeTorrentFile(int indiceLinha) {
        linhas.remove(indiceLinha);

        fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    /* Adiciona uma lista de TorrentFile ao final dos registros. */
    public void addListaDeTorrentFile(List<TorrentFile> torrent) {
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
