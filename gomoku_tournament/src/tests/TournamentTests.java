package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gomoku.Game;
import gomoku.tournament.controller.GomokuTournamentController;
import gomoku.tournament.controller.TournamentPlanMaker;
import gomoku.tournament.model.GameStats;
import gomoku.tournament.model.PlayerStats;

public class TournamentTests {

	@Test
	public void test() {
		GomokuTournamentController c = new GomokuTournamentController(new Game(10, 10));
		assertNotNull(c);
		assertNotNull(c.setStrategiesList());
		TournamentPlanMaker maker = new TournamentPlanMaker(c.setStrategiesList(), c);
		assertNotNull(maker);
		GameStats status = new GameStats("Strategy", "", 10, "");
		assertNotNull(status);
		assertEquals(new Integer(10), status.getBoardSize());
		PlayerStats stats = new PlayerStats();
		assertNotNull(stats);
		stats.setDraws(1);
		stats.setLoses(2);
		stats.setName("Dummy");
		stats.setWins(12);
		stats.setStrategy("DummyStrategy");
		assertNotNull(stats.getDraws());
		assertNotNull(stats.getLoses());
		assertNotNull(stats.getName());
		assertNotNull(stats.getStrategy());
		assertNotNull(stats.getWinRate());
		assertEquals(1, stats.getDraws());
		assertEquals(2, stats.getLoses());
		assertEquals("Dummy", stats.getName());
		assertEquals(12, stats.getWins());
		assertEquals("DummyStrategy", stats.getStrategy());
		
	}

}
