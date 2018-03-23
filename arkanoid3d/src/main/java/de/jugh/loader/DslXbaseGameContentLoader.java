package de.jugh.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;

import de.jugh.GameProperties;
import de.jugh.arkanoidDsl.BrickInRow;
import de.jugh.arkanoidDsl.BrickReference;
import de.jugh.arkanoidDsl.ContentDefinition;
import de.jugh.arkanoidDsl.GameContentProvider;
import de.jugh.arkanoidDsl.Level;
import de.jugh.arkanoidDsl.Row;
import de.jugh.arkanoidDsl.RowCreator;
import de.jugh.content.Brick;
import de.jugh.content.Game;
import de.jugh.generator.IRowGenerator;
import de.jugh.xtext.Dsl;
import de.jugh.xtext.DslProcessCtx;
import de.jugh.xtext.DslProcessingException;
import de.jugh.xtext.Generator;
import de.jugh.xtext.XTextStandaloneService;
import de.jugh.xtext.XTextStandaloneServiceManager;
import javafx.scene.paint.Color;

/**
 * The class that creates game content based on a xtext dsl.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class DslXbaseGameContentLoader extends GameContentLoader
{
	private static Logger LOG = Logger.getLogger(DslXbaseGameContentLoader.class);

	@Override
	public Game createGame()
	{
		final XTextStandaloneService xtextStandaloneService = XTextStandaloneServiceManager.get()
				.getXtextStandaloneService();
		final Set<Dsl> dsls = loadDsls();
		final DslProcessCtx ctx = new DslProcessCtx(dsls);
		try {
			xtextStandaloneService.reloadUsmDsl(ctx);
		} catch (DslProcessingException d) {
			LOG.error("Failed reloading dsl");
			return null;
		}

		final ContentDefinition dslContent = gameFromDsl(dsls, ctx.getResourceSet());
		final Game g = new Game();
		final List<de.jugh.content.Level> levels = new ArrayList<>();

		for (Level level : dslContent.getLevels()) {
			de.jugh.content.Level jfxLevel = new de.jugh.content.Level();

			for (Row row : level.getRows()) {
				de.jugh.content.Row jfxRow = new de.jugh.content.Row();
				addBricksToRow(jfxRow, row);
				jfxLevel.addRow(jfxRow);
				jfxRow.setTranslateX(-1 * (jfxRow.getBoundsInLocal().getWidth() / 2));
			}
			levels.add(jfxLevel);
		}
		levels.forEach(g::addLevel);

		return g;
	}

	private void addBricksToRow(de.jugh.content.Row jfxRow, Row source)
	{
		if (source.getRowCreatorRef() != null) {
			List<de.jugh.arkanoidDsl.Brick> bricksGenerated = bricksGenerated(source.getRowCreatorRef());
		} else {
			List<BrickInRow> bricks = new ArrayList<BrickInRow>(source.getBricks().size());
			bricks.addAll(source.getBricks());
			Collections.reverse(bricks);
			for (BrickInRow brickInRow : bricks) {
				Brick jfxBrick = new Brick();
				de.jugh.arkanoidDsl.Brick brickFromDef = getBrickFromDef(brickInRow);
				jfxBrick.color(Color.valueOf(brickFromDef.getColor().getName()));
				jfxRow.addBrick(jfxBrick);
			}
		}
	}

	private List<de.jugh.arkanoidDsl.Brick> bricksGenerated(RowCreator creator)
	{
		return generateBricks(rowGenerator(), creator);
	}

	private de.jugh.arkanoidDsl.Brick getBrickFromDef(BrickInRow bir)
	{
		if (bir instanceof BrickReference) {
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
				String readLines = IOUtils.toString(new FileInputStream(new File(srcPath, fileName)), "UTF-8");
				Dsl dsl = new Dsl(readLines, fileName);
				result.add(dsl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private ContentDefinition gameFromDsl(Set<Dsl> dsls, XtextResourceSet xtextResourceSet)
	{
		final Optional<Dsl> contetDefDsl = dsls.stream().filter(dsl -> (dsl.getGameContentProvider().getGame() != null))
				.findFirst();

		final URI dslResFileUri = Generator.dslResFileUri(contetDefDsl.get());
		final Resource resource = xtextResourceSet.getResource(dslResFileUri, false);
		GameContentProvider eObject = (GameContentProvider) resource.getContents().get(0);
		return eObject.getGame();
	}

	private IRowGenerator rowGenerator()
	{
		return XTextStandaloneServiceManager.get().getXtextStandaloneService().instance(IRowGenerator.class);
	}

	private List<de.jugh.arkanoidDsl.Brick> generateBricks(IRowGenerator rowGenerator, RowCreator creator)
	{
		final String creatorName = creator.getName();
		try {
			Method creatorMethod = rowGenerator.getClass().getMethod(creatorName);
			return (List<de.jugh.arkanoidDsl.Brick>) creatorMethod.invoke(rowGenerator);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOG.error("failed to retrieve row create method", e);
		}
		return Collections.EMPTY_LIST;
	}
}
