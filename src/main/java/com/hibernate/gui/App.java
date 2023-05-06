package com.hibernate.gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.hibernate.model.Categoria;
import com.hibernate.model.Producto;
import com.hibernate.dao.ProductoDAO;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class App {

	private JFrame frmAlmacnSupermercado;
	private JTextField textFieldId;
	private JTextField textFieldNomProd;
	private JTextField textFieldPrecio;
	private JTextField textFieldEnStock;
	
	void limpiarTexto() {
		textFieldId.setText("");
		textFieldNomProd.setText("");
		//textFieldCat.setText("");
		textFieldPrecio.setText("");
		textFieldEnStock.setText("");
	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frmAlmacnSupermercado.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		ProductoDAO productoDAO = new ProductoDAO();
		Producto producto = new Producto();
		List<Producto> productos = null;
		
		
		frmAlmacnSupermercado = new JFrame();
		frmAlmacnSupermercado.setTitle("Almacén Supermercado");
		frmAlmacnSupermercado.setBounds(100, 100, 1121, 619);
		frmAlmacnSupermercado.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAlmacnSupermercado.getContentPane().setLayout(null);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1. Pescado", "2. Carne", "3. Bebida", "4. Verdura", "5. Fruta", "6. Lácteos"}));
		comboBox.setBounds(392, 305, 180, 22);
		frmAlmacnSupermercado.getContentPane().add(comboBox);
		
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("Categoría");
		model.addColumn("Nombre");
		model.addColumn("Precio");
		model.addColumn("En Stock");
		
		productos = productoDAO.selectAllProducto();
		
		for (Producto p : productos) {
			Object[] row = new Object[5];
			row[0] = p.getIdProducto();
			int categoria = p.getCategoria();
			String categoriaString="";
			switch (categoria) {
			case 1:
				categoriaString = "Pescado";
				break;
			case 2:
				categoriaString = "Carne";break;
			case 3:
				categoriaString = "Bebida";break;
			case 4:
				categoriaString = "Verdura";break;
			case 5:
				categoriaString = "Fruta";break;
			case 6:
				categoriaString = "Lácteos";break;
			
			}
			row[1] = categoriaString;
			row[2] = p.getNomProd();
			row[3] = p.getPrecio();
			row[4] = p.getStock();
			model.addRow(row);
		}
		JTable tableProductos = new JTable(model);
		tableProductos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int opc = 0;
				int index = tableProductos.getSelectedRow();
				TableModel model = tableProductos.getModel();
				
				textFieldId.setText(model.getValueAt(index, 0).toString());
				switch (model.getValueAt(index, 1).toString()) {
				case "Pescado":
					opc = 0;
					break;
				case "Carne":
					opc = 1;
					break;
				case "Bebida":
					opc = 2;
					break;
				case "Verdura":
					opc = 3;
					break;
				case "Fruta":
					opc = 4;
					break;
				case "Lácteos":
					opc = 5;
					break;
				}
				comboBox.setSelectedIndex(opc);
				textFieldNomProd.setText(model.getValueAt(index, 2).toString());
				textFieldPrecio.setText(model.getValueAt(index, 3).toString());
				textFieldEnStock.setText(model.getValueAt(index, 4).toString());
				
			}
		    
		});
		tableProductos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		TableColumnModel columnModel = tableProductos.getColumnModel();
		
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		
		columnModel.getColumn(0).setPreferredWidth(60);
		columnModel.getColumn(0).setCellRenderer(centerRender);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(1).setCellRenderer(centerRender);
		columnModel.getColumn(2).setPreferredWidth(230);
		columnModel.getColumn(2).setCellRenderer(centerRender);
		columnModel.getColumn(3).setPreferredWidth(90);
		columnModel.getColumn(3).setCellRenderer(centerRender);
		columnModel.getColumn(4).setPreferredWidth(90);
		columnModel.getColumn(4).setCellRenderer(centerRender);
		tableProductos.setDefaultEditor(Producto.class, null);
		frmAlmacnSupermercado.getContentPane().add(tableProductos);
		
		
		JScrollPane scrollPane = new JScrollPane(tableProductos);
		scrollPane.setBounds(97, 28, 660, 222);
		frmAlmacnSupermercado.getContentPane().add(scrollPane);
		
		
		JButton btnGuardarProd = new JButton("GUARDAR");
		btnGuardarProd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Producto producto = new Producto(textFieldNomProd.getText(), Integer.parseInt(textFieldPrecio.getText()), 
						Integer.parseInt(textFieldEnStock.getText()),(comboBox.getSelectedIndex()+1)); //comboBoxCat.getToolTipText()
				productoDAO.insertProducto(producto);
				JOptionPane.showMessageDialog(null, "Producto añadido");
				limpiarTexto();
			}
		});
		btnGuardarProd.setBounds(207, 461, 122, 21);
		frmAlmacnSupermercado.getContentPane().add(btnGuardarProd);
		
		JButton btnActualizarProd = new JButton("ACTUALIZAR");
		btnActualizarProd.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        Producto productoActualizar = productoDAO.selectProductoById(Integer.parseInt(textFieldId.getText()));
		        productoActualizar.setNomProd(textFieldNomProd.getText());
		        productoActualizar.setCategoria((comboBox.getSelectedIndex()+1));
		        productoActualizar.setPrecio(Double.parseDouble(textFieldPrecio.getText()));
		        productoActualizar.setStock(Integer.parseInt(textFieldEnStock.getText()));
		        productoDAO.updateProducto(productoActualizar);
		        JOptionPane.showMessageDialog(null, "Producto actualizado");
		        limpiarTexto();
		    }
		});
		btnActualizarProd.setBounds(371, 461, 122, 21);
		frmAlmacnSupermercado.getContentPane().add(btnActualizarProd);
		
		
		JButton btnBorrarProd = new JButton("BORRAR");
		btnBorrarProd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				productoDAO.deleteProducto(Integer.parseInt(textFieldId.getText()));
				JOptionPane.showMessageDialog(null, "Producto borrado");
				//btnTablaActSinStock.doClick();
				limpiarTexto();
			}
		});
		btnBorrarProd.setBounds(529, 461, 122, 21);
		frmAlmacnSupermercado.getContentPane().add(btnBorrarProd);
		
		JLabel lblIdProd = new JLabel("ID:");
		lblIdProd.setBounds(279, 276, 70, 15);
		frmAlmacnSupermercado.getContentPane().add(lblIdProd);
		
		JLabel lblCat = new JLabel("CATEGORÍA:");
		lblCat.setBounds(279, 309, 114, 15);
		frmAlmacnSupermercado.getContentPane().add(lblCat);
		
		JLabel lblNomProd = new JLabel("NOMBRE:");
		lblNomProd.setBounds(279, 336, 70, 15);
		frmAlmacnSupermercado.getContentPane().add(lblNomProd);
		
		JLabel lblPrecio = new JLabel("PRECIO:");
		lblPrecio.setBounds(279, 363, 70, 15);
		frmAlmacnSupermercado.getContentPane().add(lblPrecio);
		
		JLabel lblStock = new JLabel("STOCK:");
		lblStock.setBounds(279, 390, 93, 15);
		frmAlmacnSupermercado.getContentPane().add(lblStock);
		
		textFieldId = new JTextField();
		textFieldId.setEnabled(false);
		textFieldId.setEditable(false);
		textFieldId.setBounds(392, 274, 180, 19);
		frmAlmacnSupermercado.getContentPane().add(textFieldId);
		textFieldId.setColumns(10);
		
		textFieldNomProd = new JTextField();
		textFieldNomProd.setColumns(10);
		textFieldNomProd.setBounds(392, 336, 180, 19);
		frmAlmacnSupermercado.getContentPane().add(textFieldNomProd);
		
		textFieldPrecio = new JTextField();
		textFieldPrecio.setColumns(10);
		textFieldPrecio.setBounds(392, 361, 180, 19);
		frmAlmacnSupermercado.getContentPane().add(textFieldPrecio);
		
		textFieldEnStock = new JTextField();
		textFieldEnStock.setColumns(10);
		textFieldEnStock.setBounds(392, 386, 180, 19);
		frmAlmacnSupermercado.getContentPane().add(textFieldEnStock);
		
		
		
	}
}