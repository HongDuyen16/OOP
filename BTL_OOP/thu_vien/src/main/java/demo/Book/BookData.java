package demo.Book;

import javax.swing.table.DefaultTableModel;

import demo.Functions.MySqlConnection;

import java.sql.*;
import java.util.Vector;

// import Functions.MySqlConnection;

public class BookData {
    private static BookData instance;
    private DefaultTableModel physicalBookTableModel;
    private DefaultTableModel eBookTableModel;

    private BookData() {
        // Mô hình bảng cho sách in
        String[] physicalBookColumns = {"ID", "Tên sách", "Tác giả", "Năm xuất bản", "Số lượng", "Hành động"};
        physicalBookTableModel = new DefaultTableModel(physicalBookColumns, 0);

        // Mô hình bảng cho sách điện tử
        String[] eBookColumns = {"ID", "Tên sách", "Tác giả", "Năm xuất bản", "Định dạng file", "Kích thước file", "Hành động"};
        eBookTableModel = new DefaultTableModel(eBookColumns, 0);

        loadBooksFromDatabase();
    }

    public static synchronized BookData getInstance() {
        if (instance == null) {
            instance = new BookData();
        }
        return instance;
    }

    public DefaultTableModel getPhysicalBookTableModel() {
        return physicalBookTableModel;
    }

    public DefaultTableModel getEBookTableModel() {
        return eBookTableModel;
    }

    public void addPhysicalBook(Object[] rowData) {
        physicalBookTableModel.addRow(rowData);
    }

    public void addEBook(Object[] rowData) {
        eBookTableModel.addRow(rowData);
    }

    public void removePhysicalBook(int row) {
        int id = (int) physicalBookTableModel.getValueAt(row, 0);
        deletePhysicalBookFromDatabase(id);
        physicalBookTableModel.removeRow(row);
    }

    public void removeEBook(int row) {
        int id = (int) eBookTableModel.getValueAt(row, 0);
        deleteEBookFromDatabase(id);
        eBookTableModel.removeRow(row);
    }

    private void loadBooksFromDatabase() {
        try (Connection connection = MySqlConnection.getConnection()) {
            loadPhysicalBooks(connection);
            loadEBooks(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPhysicalBooks(Connection connection) throws SQLException {
        String query = "SELECT * FROM ThuVienBook";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("ID"));
                row.add(rs.getString("TieuDeSach"));
                row.add(rs.getString("TacGia"));
                row.add(rs.getInt("NamXuatBan"));
                row.add(rs.getInt("SoLuong"));
                // row.add("Delete");
                physicalBookTableModel.addRow(row);
            }
        }
    }

    private void loadEBooks(Connection connection) throws SQLException {
        String query = "SELECT * FROM ThuVienEBook";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("ID"));
                row.add(rs.getString("TenSach"));
                row.add(rs.getString("TacGia"));
                row.add(rs.getInt("NamXuatBan"));
                row.add(rs.getString("DinhDangFile"));
                row.add(rs.getString("KichThuocFile"));
                // row.add("Delete");
                eBookTableModel.addRow(row);
            }
        }
    }

    private void deletePhysicalBookFromDatabase(int id) {
        try (Connection connection = MySqlConnection.getConnection()) {
            String query = "DELETE FROM ThuVienBook WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEBookFromDatabase(int id) {
        try (Connection connection = MySqlConnection.getConnection()) {
            String query = "DELETE FROM ThuVienEBook WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // private void updateBookQuantity(String bookId, int newQuantity) {
    //     try (Connection connection = MySqlConnection.getConnection()) {
    //         if (newQuantity == 0) {
    //             deleteBook(bookId);
    //         } else {
    //             String updateQuery = "UPDATE ThuVienBook SET SoLuong = ? WHERE ID = ?";
    //             try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
    //                 updateStatement.setInt(5, newQuantity);
    //                 updateStatement.setString(1, bookId);
    //                 updateStatement.executeUpdate();
    //             }
    //         }
    //     } catch (SQLException ex) {
    //         ex.printStackTrace();
    //     }
    // }
    // private void deleteBook(String bookId) {
    // }
}
