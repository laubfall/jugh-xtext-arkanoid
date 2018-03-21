package de.jugh.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import de.jugh.GameProperties;
import de.jugh.arkanoidDsl.BrickInRow;
import de.jugh.arkanoidDsl.BrickReference;
import de.jugh.arkanoidDsl.ContentDefinition;
import de.jugh.arkanoidDsl.Level;
import de.jugh.arkanoidDsl.Row;
import de.jugh.xtext.Dsl;
import de.jugh.xtext.DslProcessCtx;
import de.jugh.xtext.XTextStandaloneService;
import de.jugh.xtext.XTextStandaloneServiceManager;
import javafx.scene.paint.Color;

/**
 * The class that creates game content based on a xtext dsl.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class DslGameContentLoader extends GameContentLoader
{

	@Override
	public Game createGame()
	{
		final XTextStandaloneService xtextStandaloneService = XTextStandaloneServiceManager.get()
				.getXtextStandaloneService();
		final Set<Dsl> dsls = loadDsls();
		final DslProcessCtx ctx = new DslProcessCtx(dsls);
		xtextStandaloneService.reloadUsmDsl(ctx);

		final ContentDefinition dslContent = gameFromDsl(dsls);
		final Game g = new Game();
		for (Level level : dslContent.getLevels()) {
			de.jugh.content.Level rl = new de.jugh.content.Level();

			for (Row row : level.getRows()) {
				de.jugh.content.Row rr = new de.jugh.content.Row();
				for (BrickInRow brickInRow : row.getBricks()) {
					Brick brick = new Brick();
					de.jugh.arkanoidDsl.Brick brickFromDef = getBrickFromDef(brickInRow);
					
					brick.color(Color.valueOf(brickFromDef.getColor().getName()));
					rr.addBrick(brick);
				}
				rl.addRow(rr);
			}

			g.addLevel(rl);
		}
		return g;
	}

	private de.jugh.arkanoidDsl.Brick getBrickFromDef(BrickInRow bir)
	{
		if(bir instanceof BrickReference) {
			BrickReference br = (BrickReference) bir;
			return br.getReference();
		}
		return bir.getInstance();
	}

	private Set<Dsl> loadDsls()
	{
		final String srcPath = GameProperties.get().getProperty("game.dsl.srcpath");
		final String dslFileExt = GameProperties.get().getProperty("game.dsl.fileext");
		final String[] list = new File(srcPath).list((File file, String name) -> {
			return name.endsWith(dslFileExt);
		});

		final Set<Dsl> result = new HashSet<Dsl>();
		for (String fileName : list) {
			try {
				String readLines = IOUtils.toString(new FileInputStream(new File(srcPath,fileName)), "UTF-8");
				Dsl dsl = new Dsl(readLines, fileName);
				result.add(dsl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private ContentDefinition gameFromDsl(Set<Dsl> dsls)
	{
		final Optional<Dsl> contetDefDsl = dsls.stream().filter(dsl -> (dsl.getGameContentProvider().getGame() != null))
				.findFirst();
		return contetDefDsl.get().getGameContentProvider().getGame();
	}
}
