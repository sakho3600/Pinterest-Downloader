package nl.juraji.pinterestdownloader.ui.workers;

import nl.juraji.pinterestdownloader.configuration.UIConfig;
import nl.juraji.pinterestdownloader.io.ApiHandler;
import nl.juraji.pinterestdownloader.io.FileTypeFixer;
import nl.juraji.pinterestdownloader.io.PinsDownloader;
import nl.juraji.pinterestdownloader.model.pinterest.objects.Board;
import nl.juraji.pinterestdownloader.model.pinterest.objects.Pin;
import nl.juraji.pinterestdownloader.ui.controllers.LockUIController;
import nl.juraji.pinterestdownloader.ui.controllers.ProgressController;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessWorker {

  private final UIConfig config = UIConfig.getInstance();
  private final ProgressController progressController;
  private final LockUIController lockUIController;
  private final ExecutorService executorService;
  private final ApiHandler apiHandler;

  public ProcessWorker(ProgressController progressController, LockUIController lockUIController) {
    this.progressController = progressController;
    this.lockUIController = lockUIController;
    this.executorService = Executors.newSingleThreadExecutor();
    this.apiHandler = ApiHandler.getInstance();
  }

  public void start() {
    // Submit prepare UI for process start task
    executorService.submit(() -> {
      lockUIController.lock();
      progressController.pushNewProcess("Starting...");
    });

    // Submit downloading boards task
    if (config.isDoBoardDownload()) {
      executorService.submit(this::downloadBoards);
    }

    // Submit file check task
    if (config.isDoFileCheck()) {
      executorService.submit(this::checkDownloadedFiles);
    }

    // Submit open in file browser task
    executorService.submit(() -> {
      if (config.isOpenOnCompletion()) {
        this.openTarget();
      }
    });

    // Submit updating UI for end of process task
    executorService.submit(() -> {
      progressController.pushNewProcess("Done!");
      lockUIController.unlock();
    });
  }

  private void downloadBoards() {
    progressController.pushNewProcess("Fetching Boards...");
    List<Board> boards = apiHandler.getMyBoards();

    progressController.pushNewProcess("Found " + boards.size() + " Boards!");
    boards.forEach(this::processBoard);
  }

  private void processBoard(Board board) {
    progressController.pushNewProcess("Processing " + board.getName() + "...");
    PinsDownloader downloader = new PinsDownloader(board, config.getDownloadTarget(), progressController::doTick);

    long diff = new Date().getTime() - downloader.getBoardDir().lastModified();
    if (config.isDoSkipRecentBoards() && diff > 86400000) {
      progressController.pushNewProcess("Retrieving pins for " + board.getName() + "...");
      List<Pin> pins = apiHandler.getPinsForBoard(board);

      if (pins.size() == 0) return;
      progressController.pushNewProcess("Downloading " + pins.size() + " pins on board " + board.getName() + "...", pins.size());

      try {
        downloader.start(pins);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void checkDownloadedFiles() {
    progressController.pushNewProcess("Listing files to check...");
    FileTypeFixer fileTypeFixer = new FileTypeFixer(config.getDownloadTarget(), progressController::doTick);
    progressController.pushNewProcess("Checking and repairing " + fileTypeFixer.getFileCount() + " files...", fileTypeFixer.getFileCount());
    fileTypeFixer.startScan();
  }

  private void openTarget() {
    if (Desktop.isDesktopSupported()) try {
      Desktop.getDesktop().browse(config.getDownloadTarget().toURI());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
