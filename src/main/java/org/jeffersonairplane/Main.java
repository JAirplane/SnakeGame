package org.jeffersonairplane;

import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;
import org.jeffersonairplane.viewmodel.*;

public class Main {
	
    public static void main(String[] args) {

		GameView view = new GameViewImpl();
		var model = new GameModelImpl();
		model.setPowerUpTypesCreationChances();
        GameViewModel gameViewModel = new GameViewModelImpl(view, model);

		gameViewModel.runGameplay();
    }
}