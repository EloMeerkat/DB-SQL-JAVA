package persistanceManager.dbConnect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import persistanceManager.annotations.DBBean;
import persistanceManager.annotations.DBLink;
import persistanceManager.annotations.Ignore;

public class DataBase {
	private Connection connection;
	private Statement statement;
	public String url;
	public String userID;
	public String password;

	public DataBase(String url, String userID, String password) {
		this.url = url;
		this.userID = userID;
		this.password = password;
		this.DBConnect();
	}

	public void DBConnect() {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(url, userID, password);
				statement = connection.createStatement();
				System.out.println("Connected to the database :)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet sendRequest(String sql) {
		ResultSet result = null;
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(url, userID, password);
				statement = connection.createStatement();
				System.out.println("Connected to the database :)");
			}
			result = statement.executeQuery(sql);
		} catch (SQLException e) {
			if (e.getSQLState().equals("23505")) {
				System.out.println("La clé primaire de la donnée à ajouter existe déjà.");
			} else if (e.getSQLState().equals("02000")) {
				// Ignore
			} else
				e.printStackTrace();
		}

		return result;
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				/* Fermeture de la connexion */
				connection.close();
				System.out.println("Database disconnected :)");
			} catch (SQLException e) {
				// Ignore
			}
		}
	}

	public boolean isConnected() {
		return (connection == null);
	}

	public <T> List<T> retreiveList(Class<T> beanClass, String sql) {
		String table;
		ResultSet result = null;

		if (beanClass.isAnnotationPresent(DBBean.class)) {
			DBBean annot = ((DBBean) beanClass.getAnnotation(DBBean.class));
			table = annot.bean();
			sql = "SELECT " + getFields(beanClass) + " FROM " + table + sql + ";";

			// System.out.println(sql);
			result = sendRequest(sql);
		}
		return toList(beanClass, result);
	}

	private <T> List<T> toList(Class<T> c, ResultSet result) {
		List<T> list = new ArrayList<>();
		List<Integer> id = new ArrayList<Integer>();
		ResultSet r = result;
		int i = 0;
		try {
			while (r.next()) {
				T tempVal = c.newInstance();
				for (Field field : c.getFields()) {
					if (field.isAnnotationPresent(Ignore.class)) {
						Ignore fieldAnnot = ((Ignore) field.getAnnotation(Ignore.class));
						DBBean fieldBean = ((DBBean) c.getAnnotation(DBBean.class));
						if (!fieldAnnot.isIgnored()) {
							if (field.isAnnotationPresent(DBLink.class)) {
								// Ignore
							} else {
								if (field.getType() == int.class) {
									field.set(tempVal, r.getInt(field.getName().toLowerCase()));
									if (field.getName().equals(fieldBean.primaryKey())) {
										id.add(r.getInt(field.getName().toLowerCase()));
									}
								} else if (field.getType() == String.class) {
									field.set(tempVal, r.getString(field.getName().toLowerCase()));
								}
							}
						}
					}
				}

				list.add(tempVal);
				i++;
			}
			while (i != 0) {
				i--;
				T tempVal = list.get(i);
				for (Field field : c.getFields()) {
					if (field.isAnnotationPresent(Ignore.class)) {
						Ignore fieldAnnot = ((Ignore) field.getAnnotation(Ignore.class));
						DBBean fieldBean = ((DBBean) c.getAnnotation(DBBean.class));
						if (!fieldAnnot.isIgnored()) {
							if (field.isAnnotationPresent(DBLink.class)) {
								String sql = " WHERE " + fieldBean.primaryKey() + " = " + id.get(i);
								ParameterizedType ListType = (ParameterizedType) field.getGenericType();
								Class<?> ListClass = (Class<?>) ListType.getActualTypeArguments()[0];
								field.set(tempVal, retreiveList(ListClass, sql));
							}
						}
					}
				}
				list.set(i, tempVal);
			}
		} catch (SQLException e) {
			System.out.println(e.getSQLState());
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException | SecurityException e) {

		}
		return list;
	}

	private <T> String getFields(Class<T> c) {
		StringBuilder sb = new StringBuilder();

		for (Field field : c.getFields()) {
			if (field.isAnnotationPresent(Ignore.class)) {
				Ignore fieldAnnot = ((Ignore) field.getAnnotation(Ignore.class));

				if (!fieldAnnot.isIgnored()) {
					if (field.isAnnotationPresent(DBLink.class)) {
						//DBLink LinkAnnot = ((DBLink) field.getAnnotation(DBLink.class));
						// System.out.println(LinkAnnot.beanName());
					} else {
						sb.append(field.getName().toLowerCase()).append(",");
					}
				}
			}
		}
		return sb.delete(sb.length() - 1, sb.length()).toString();
	}

	private <T> String getValues(T obj) {
		StringBuilder sb = new StringBuilder();
		Class<?> c = obj.getClass();

		for (Field field : c.getFields()) {
			if (field.isAnnotationPresent(Ignore.class)) {
				Ignore fieldAnnot = ((Ignore) field.getAnnotation(Ignore.class));

				if (!fieldAnnot.isIgnored()) {
					if (field.isAnnotationPresent(
							DBLink.class)) {/*
											 * try { if (field.get(obj) != null) bulkInsert((List<?>) field.get(obj)); }
											 * catch (IllegalArgumentException | IllegalAccessException e) {
											 * e.printStackTrace(); }
											 */
					} else {
						try {
							sb.append("'").append(field.get(obj)).append("',");
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return sb.delete(sb.length() - 1, sb.length()).toString();
	}

	private <T> void insertBlinkList(T obj) {
		Class<?> c = obj.getClass();
		for (Field field : c.getFields()) {
			if (field.isAnnotationPresent(Ignore.class)) {
				Ignore fieldAnnot = ((Ignore) field.getAnnotation(Ignore.class));

				if (!fieldAnnot.isIgnored()) {
					if (field.isAnnotationPresent(DBLink.class)) {
						try {
							if (field.get(obj) != null)
								bulkInsert((List<?>) field.get(obj));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public <T> int insert(T bean) {
		String table, sql;
		if (bean.getClass().isAnnotationPresent(DBBean.class)) {
			DBBean annot = ((DBBean) bean.getClass().getAnnotation(DBBean.class));
			table = annot.bean();
			sql = "INSERT INTO " + table + " (" + getFields(bean.getClass()) + ")" + " VALUES (" + getValues(bean)
					+ ");";
			sendRequest(sql);
		}
		return 0;
	}

	public <T> int bulkInsert(List<T> b) {
		String table, sql;
		sql = "BEGIN; ";
		T bean;
		for (int i = 0; i < b.size(); i++) {
			bean = b.get(i);
			if (bean.getClass().isAnnotationPresent(DBBean.class)) {
				DBBean annot = ((DBBean) bean.getClass().getAnnotation(DBBean.class));
				table = annot.bean();
				sql += "INSERT INTO " + table + " (" + getFields(bean.getClass()) + ")" + " VALUES (" + getValues(bean)
						+ "); ";
			}
			// insert(b.get(i));
		}
		sql += "COMMIT;";
		sendRequest(sql);
		System.out.println(sql);

		for (int i = 0; i < b.size(); i++) {
			bean = b.get(i);
			insertBlinkList(bean);
		}
		
		return 0;
	}
}
