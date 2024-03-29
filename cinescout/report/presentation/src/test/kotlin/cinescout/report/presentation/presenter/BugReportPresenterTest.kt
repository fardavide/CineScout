package cinescout.report.presentation.presenter

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import arrow.core.none
import cinescout.report.domain.usecase.FakeBuildReportLinks
import cinescout.report.presentation.action.BugReportAction
import cinescout.report.presentation.model.BugReportField
import cinescout.report.presentation.model.TextFieldState
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.test.android.MoleculeTestExtension
import cinescout.test.kotlin.expectLastItem
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class BugReportPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension())

    Given("presenter") {

        When("started") {
            val scenario = TestScenario()

            Then("title is empty") {
                scenario.flow.test {
                    expectLastItem().title shouldBe TextFieldState.Empty
                }
            }

            Then("description is empty") {
                scenario.flow.test {
                    expectLastItem().description shouldBe TextFieldState.Empty
                }
            }
        }

        When("submit") {
            val scenario = TestScenario(
                actions = flow {
                    val submit = BugReportAction.Submit(
                        description = "",
                        expectedBehavior = "",
                        steps = "",
                        title = ""
                    )
                    emit(submit)
                }
            )

            Then("all fields are validated") {
                scenario.flow.test {
                    with(expectLastItem()) {
                        title.error.getOrNull() shouldBe TextRes(string.report_error_empty_title)
                        description.error.getOrNull() shouldBe TextRes(string.report_error_empty_description)
                        expectedBehavior.error.getOrNull() shouldBe TextRes(string.report_error_empty_expected_behavior)
                        steps.error.getOrNull() shouldBe TextRes(string.report_error_empty_steps)
                    }
                }
            }
        }
    }

    Given("title updated") {

        When("title is empty") {
            val scenario = TestScenario(
                validateField = BugReportAction.ValidateField(BugReportField.Title, "")
            )

            Then("title is empty") {
                scenario.flow.test {
                    expectLastItem().title.text shouldBe ""
                }
            }

            Then("title has error") {
                scenario.flow.test {
                    expectLastItem().title.error.getOrNull() shouldBe TextRes(string.report_error_empty_title)
                }
            }
        }

        When("title is focused") {
            val scenario = TestScenario(
                focusedField = BugReportField.Title
            )

            Then("title has no error") {
                scenario.flow.test {
                    expectLastItem().title.error shouldBe none()
                }
            }
        }
    }

    Given("description updated") {

        When("description is empty") {
            val scenario = TestScenario(
                validateField = BugReportAction.ValidateField(BugReportField.Description, "")
            )

            Then("description has error") {
                scenario.flow.test {
                    expectLastItem().description.error.getOrNull() shouldBe
                        TextRes(string.report_error_empty_description)
                }
            }
        }

        When("description is focused") {
            val scenario = TestScenario(
                focusedField = BugReportField.Description
            )

            Then("description has no error") {
                scenario.flow.test {
                    expectLastItem().description.error shouldBe none()
                }
            }

            And("another field is empty") {

                Then("the other fields doesn't have error") {
                    scenario.flow.test {
                        with(expectLastItem()) {
                            title.error shouldBe none()
                            steps.error shouldBe none()
                            expectedBehavior.error shouldBe none()
                        }
                    }
                }
            }
        }
    }

    Given("steps updated") {

        When("steps is empty") {
            val scenario = TestScenario(
                validateField = BugReportAction.ValidateField(BugReportField.Steps, "")
            )

            Then("steps has error") {
                scenario.flow.test {
                    expectLastItem().steps.error.getOrNull() shouldBe TextRes(string.report_error_empty_steps)
                }
            }
        }

        When("steps is focused") {
            val scenario = TestScenario(
                focusedField = BugReportField.Steps
            )

            Then("steps has no error") {
                scenario.flow.test {
                    expectLastItem().steps.error shouldBe none()
                }
            }
        }
    }

    Given("expected behavior updated") {

        When("expected behavior is empty") {
            val scenario = TestScenario(
                validateField = BugReportAction.ValidateField(BugReportField.ExpectedBehavior, "")
            )

            Then("expected behavior is empty") {
                scenario.flow.test {
                    expectLastItem().expectedBehavior.text shouldBe ""
                }
            }

            Then("expected behavior has error") {
                scenario.flow.test {
                    expectLastItem().expectedBehavior.error.getOrNull() shouldBe
                        TextRes(string.report_error_empty_expected_behavior)
                }
            }
        }

        When("expected behavior is focused") {
            val scenario = TestScenario(
                focusedField = BugReportField.ExpectedBehavior
            )

            Then("expected behavior has no error") {
                scenario.flow.test {
                    expectLastItem().expectedBehavior.error shouldBe none()
                }
            }
        }
    }
})

private class BugReportTestScenario(
    actionsFlow: Flow<BugReportAction>,
    val sut: BugReportPresenter
) {

    val flow = moleculeFlow(mode = RecompositionMode.Immediate) {
        sut.models(actions = actionsFlow)
    }.distinctUntilChanged()
}

private fun TestScenario(
    focusedField: BugReportField? = null,
    validateField: BugReportAction.ValidateField? = null,
    actions: Flow<BugReportAction> = flow {
        focusedField?.let { emit(BugReportAction.FocusChanged(it)) }
        validateField?.let { emit(it) }
    }
) = BugReportTestScenario(
    actionsFlow = actions,
    sut = BugReportPresenter(FakeBuildReportLinks())
)
