package controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.exceptions.FullLobbyExeption;
import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.network.server.Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServerControllerTest {

    @Test
    @DisplayName("Create lobby")
    public void createLobbyTest() {
        String username = "mario";
        int numPlayers = 4;
        int id = 123;

        // Mock dependencies
        Server serverMock = mock(Server.class);

        // Create the class under test with the mocked dependencies
        ServerController serverController = spy(new ServerController(serverMock));

        // Define behavior for mocks
        doReturn(true).when(serverController).joinLobby(eq(username), any(Controller.class));

        // Call the method under test
        Controller result = serverController.createLobby(username, numPlayers, id);

        // Verify interactions and assert results
        assertNotNull(result);
    }

    @Test
    @DisplayName("Join lobby")
    public void joinLobbyTest() throws Exception{
        String username = "luigi";

        // Mock dependencies
        Server serverMock = mock(Server.class);
        Connection connectionMock = mock(Connection.class);
        Controller controllerMock = mock(Controller.class);

        // Create the class under test with the mocked dependencies
        ServerController serverController = new ServerController(serverMock);

        // Define behavior for mocks
        when(serverMock.getClientFromName(username)).thenReturn(connectionMock);
        when(controllerMock.joinGame(connectionMock)).thenReturn(true);

        // Call the method under test
        boolean result = serverController.joinLobby(username, controllerMock);

        // Verify interactions and assert results
        assertTrue(result);
        verify(serverMock, times(1)).getClientFromName(username);
        verify(controllerMock, times(1)).joinGame(connectionMock);
    }

    @Test
    @DisplayName("Join lobby when lobby is full")
    public void joinFullLobby() throws Exception {
        // Mocking dependencies
        String username = "luigi";
        Server serverMock = mock(Server.class);
        Connection connectionMock = mock(Connection.class);
        Controller controllerMock = mock(Controller.class);

        // Create the class under test with the mocked dependencies
        ServerController serverController = new ServerController(serverMock);

        // Define behavior for mocks
        when(serverMock.getClientFromName(username)).thenReturn(connectionMock);
        when(controllerMock.joinGame(connectionMock)).thenThrow(FullLobbyExeption.class);

        // Call the method under test
        boolean result = serverController.joinLobby(username, controllerMock);

        // Verify interactions and assert results
        assertFalse(result);
        verify(serverMock, times(1)).getClientFromName(username);
        verify(controllerMock, times(1)).joinGame(connectionMock);
        verify(serverMock, times(1)).removePlayers(username);
        verify(serverMock, times(1)).login(connectionMock);
    }
}
