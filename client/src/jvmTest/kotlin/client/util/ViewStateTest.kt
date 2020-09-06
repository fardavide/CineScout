package client.util

import client.ViewStateFlow
import client.ViewStatePublisher
import util.test.CoroutinesTest

/**
 * Implement this for tests that have to publish to [ViewStateFlow]
 * Implements [CoroutinesTest] and [ViewStatePublisher]
 */
interface ViewStateTest : CoroutinesTest, ViewStatePublisher
