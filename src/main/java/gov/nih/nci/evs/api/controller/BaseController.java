
package gov.nih.nci.evs.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;

/**
 * Controller for /metadata endpoints.
 */
@RestController
@RequestMapping("${nci.evs.application.contextPath}")
@Api(tags = "Metadata endpoints")
public class BaseController {

  /** The Constant log. */
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

  /**
   * Handle exception.
   *
   * @param e the e
   */
  public void handleException(final Exception e) throws Exception {
    if (e instanceof ResponseStatusException) {
      throw e;
    }

    logger.error("Unexpected error", e);
    final String errorMessage =
        "An error occurred in the system. Please contact the NCI help desk.";
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
  }

}
