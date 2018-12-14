package nure.nick.labs.lab1.db;


import nure.nick.labs.lab1.element.User;
import nure.nick.labs.lab1.element.Group;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBConnector {

    private static volatile DBConnector instance;

    private static final String connString = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String login = "C##NICK";
    private static final String password = "myOracle721";

    private static OracleDataSource ods;


    private DBConnector() {
        Connection conn = null;


        try {
            /*Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());*/


            ods = new OracleDataSource();

            ods.setURL(connString);
            ods.setUser(login);
            ods.setPassword(password);
            conn = ods.getConnection();
        } catch (SQLException ex) {
            // to do
            System.out.println(ex);
        }

        System.out.println(conn);
        // viewUsers(conn);

    }

    public static DBConnector getInstance() {
        if (instance == null) {
            synchronized (DBConnector.class) {
                if (instance == null) {
                    instance = new DBConnector();
                }
            }
        }
        return instance;
    }


    public void addUser(String login, String password) {
        String queryMaxIDValue = "SELECT MAX(USERT_ID) MAX FROM USERT";
        String insertUser = "INSERT INTO USERT VALUES (?,?,?)";

        insertQueryBody(queryMaxIDValue, insertUser, login, password);
    }


    public LinkedList<User> getAllUsers() {
        LinkedList<User> usersList = new LinkedList<>();

        final String selectAllUsers = "SELECT * FROM USERT ORDER BY USERT_ID";

        Connection con = null;
        try {
            con = ods.getConnection();

            Statement stmtBefore = con.createStatement();

            ResultSet rs = stmtBefore.executeQuery(selectAllUsers);

            User newUser;

            while (rs.next()) {
                newUser = new User(
                        rs.getString("USERT_ID"),
                        rs.getString("LOGIN"),
                        rs.getString("PASS")
                );

                usersList.add(newUser);
            }


        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }

        return usersList;
    }

    public List<Group> getAllGroups() {
        List<Group> groupsList = new LinkedList<>();

        final String selectAllGroups = "SELECT * FROM GROUPT ORDER BY GROUPT_ID";

        Connection con = null;
        try {
            con = ods.getConnection();

            Statement stmtBefore = con.createStatement();

            ResultSet rs = stmtBefore.executeQuery(selectAllGroups);

            Group newGroup;

            while (rs.next()) {
                newGroup = new Group(
                        rs.getString("GROUPT_ID"),
                        rs.getString("NAME"),
                        rs.getString("DESCRIPT")
                );

                groupsList.add(newGroup);
            }


        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }

        return groupsList;
    }

    public void addGroup(String name, String descript) {
        String queryMaxIDValue = "SELECT MAX(GROUPT_ID) MAX FROM GROUPT";
        String insertGroup = "INSERT INTO GROUPT VALUES (?,?,?)";

        insertQueryBody(queryMaxIDValue, insertGroup, name, descript);
    }

    private void insertQueryBody(String queryMaxIDValue, String insertQury, String value1, String value2) {
        Connection con = null;
        try {
            con = ods.getConnection();

            Statement stmtBefore = con.createStatement();

            ResultSet rs = stmtBefore.executeQuery(queryMaxIDValue);

            String maxId = null;

            while (rs.next()) {
                maxId = rs.getString("MAX");

                System.out.println(maxId);
            }

            if (maxId == null) {
                maxId = "0";
            }


            PreparedStatement preparedStmt = con.prepareStatement(insertQury);

            preparedStmt.setInt(1, Integer.valueOf(maxId) + 1);
            preparedStmt.setString(2, value1);
            preparedStmt.setString(3, value2);

            preparedStmt.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }
    }

    public void deleteUser(String userId) {

        String deleteUser = "DELETE FROM USERT WHERE USERT_ID=?";

        executeSingleUpdate(deleteUser, userId);
    }

    public void deleteGroup(String groupId) {
        String deleteUser = "DELETE FROM GROUPT WHERE GROUPT_ID=?";

        executeSingleUpdate(deleteUser, groupId);
    }

    private void executeSingleUpdate(String query, String... values) {
        Connection con = null;

        try {
            con = ods.getConnection();

            PreparedStatement preparedStmt = con.prepareStatement(query);

            preparedStmt.setInt(1, Integer.parseInt(values[0]));

            if (values.length == 2) {
                preparedStmt.setInt(2, Integer.parseInt(values[1]));
            }

            if (values.length == 3) {
                preparedStmt.setString(2, values[1]);
                preparedStmt.setString(3, values[2]);
            }

            preparedStmt.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }
    }

    public List<Group> getAllGroupsOfUser(String userId) {
        List<Group> groupsList = new LinkedList<>();

        String selectAllGroups = "SELECT GROUPT.GROUPT_ID, GROUPT.NAME, GROUPT.DESCRIPT  " +
                "FROM GROUPT, USERSGROUP " +
                "WHERE GROUPT.GROUPT_ID = USERSGROUP.GROUPT_ID " +
                "AND USERSGROUP.USERT_ID = ? " +
                "ORDER BY GROUPT_ID";

        Connection con = null;

        try {
            con = ods.getConnection();

            PreparedStatement preparedStmt = con.prepareStatement(selectAllGroups);

            preparedStmt.setInt(1, Integer.parseInt(userId));

            ResultSet rs = preparedStmt.executeQuery();

            Group newGroup;

            while (rs.next()) {
                newGroup = new Group(
                        rs.getString("GROUPT_ID"),
                        rs.getString("NAME"),
                        rs.getString("DESCRIPT")
                );

                groupsList.add(newGroup);
            }


        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }

        return groupsList;
    }

    public void addNewGroupToUser(String groupId, String userId) {
        String addNewGroupToUserInsert = "INSERT INTO USERSGROUP VALUES";
        String queryMaxIDValue = "SELECT MAX(USERS_GROUP_ID) MAX FROM USERSGROUP";
        String insertGroup = "INSERT INTO USERSGROUP VALUES (?,?,?)";

        Connection con = null;
        try {
            con = ods.getConnection();

            Statement stmtBefore = con.createStatement();

            ResultSet rs = stmtBefore.executeQuery(queryMaxIDValue);

            String maxId = null;

            while (rs.next()) {
                maxId = rs.getString("MAX");

                System.out.println(maxId);
            }

            if (maxId == null) {
                maxId = "0";
            }


            PreparedStatement preparedStmt = con.prepareStatement(insertGroup);

            preparedStmt.setInt(1, Integer.valueOf(maxId) + 1);
            preparedStmt.setInt(2, Integer.valueOf(userId));
            preparedStmt.setInt(3, Integer.valueOf(groupId));

            preparedStmt.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }
    }

    public void deleteUsersGroup(String usersIdSelected, String groupIdSelected) {
        String deleteUsersGroupQuery = "DELETE FROM USERSGROUP WHERE USERT_ID=? AND GROUPT_ID=?";

        executeSingleUpdate(deleteUsersGroupQuery, usersIdSelected, groupIdSelected);
    }

    public List<User> getAllUsersOfGroup(String groupId) {
        List<User> usersList = new LinkedList<>();

        String selectAllGroups = "SELECT USERT.USERT_ID, USERT.LOGIN, USERT.PASS  " +
                "FROM USERT, USERSGROUP " +
                "WHERE USERT.USERT_ID = USERSGROUP.USERT_ID " +
                "AND USERSGROUP.GROUPT_ID = ? " +
                "ORDER BY USERT_ID";

        Connection con = null;

        try {
            con = ods.getConnection();

            PreparedStatement preparedStmt = con.prepareStatement(selectAllGroups);

            preparedStmt.setInt(1, Integer.parseInt(groupId));

            ResultSet rs = preparedStmt.executeQuery();

            User newUser;

            while (rs.next()) {
                newUser = new User(
                        rs.getString("USERT_ID"),
                        rs.getString("LOGIN"),
                        rs.getString("PASS")
                );

                usersList.add(newUser);
            }


        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }

        return usersList;
    }

    public User getUserByLogin(String login) {
        User user = new User();

        String getUser = "SELECT * FROM USERT WHERE LOGIN = ?";

        Connection con = null;
        try {
            con = ods.getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(getUser);

            preparedStatement.setString(1, login);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                user = new User(
                        rs.getString("USERT_ID"),
                        rs.getString("LOGIN"),
                        rs.getString("PASS")
                );
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }

        return user;
    }

    public void updateUser(String id, String login, String password) {
        String editUser = "{call edit_user(?, ?, ?)}";

        executeProcedure(editUser, id, login, password);
    }

    public void updateGroup(String groupId, String groupName, String groupDescription) {
        String updateGroup = "{call edit_group(?, ?, ?)}";

        executeProcedure(updateGroup, groupId, groupName, groupDescription);
    }

    private void executeProcedure(String query, String id, String val1, String val2) {
        Connection con = null;
        try {
            con = ods.getConnection();

            CallableStatement statement = con.prepareCall(query);
            statement.setInt(1, Integer.parseInt(id));
            statement.setString(2, val1);
            statement.setString(3, val2);
            statement.execute();

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }
    }

    public int getNumberOfUsers() {
        int numberOfUsers = 0;

        Connection con = null;
        try {
            con = ods.getConnection();

            CallableStatement stmt = con.prepareCall("{? = call get_number_of_users()}");

            stmt.registerOutParameter(1, Types.INTEGER);

            stmt.execute();

            numberOfUsers = stmt.getInt(1);

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignore) {
            }
        }
        return numberOfUsers;
    }
}
