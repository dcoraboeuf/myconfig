package net.myconfig.service.token;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class TokenCleanupTaskTest {

	private TokenService tokenService;
	private TokenCleanupTask task;

	@Before
	public void before() {
		tokenService = mock(TokenService.class);
		task = new TokenCleanupTask(tokenService);
	}

	@Test
	public void task() {
		task.cleanupTrigger();
		verify(tokenService, times(1)).cleanup();
	}

}
