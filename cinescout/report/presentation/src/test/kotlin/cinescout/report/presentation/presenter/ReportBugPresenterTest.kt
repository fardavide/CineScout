package cinescout.report.presentation.presenter

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import arrow.core.none
import arrow.core.some
import arrow.optics.copy
import cinescout.report.presentation.action.ReportBugAction
import cinescout.report.presentation.model.ReportBugField
import cinescout.report.presentation.model.TextFieldState
import cinescout.report.presentation.model.error
import cinescout.report.presentation.state.ReportBugState
import cinescout.report.presentation.state.description
import cinescout.report.presentation.state.expectedBehavior
import cinescout.report.presentation.state.steps
import cinescout.report.presentation.state.title
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.test.android.MoleculeTestExtension
import cinescout.test.kotlin.awaitLastItem
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class ReportBugPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension())

    Given("presenter") {

        When("started") {
            val scenario = TestScenario()

            Then("submit is disabled") {
                scenario.flow.test {
                    awaitLastItem().isSubmitEnabled shouldBe false
                }
            }

            Then("title is empty") {
                scenario.flow.test {
                    awaitLastItem().title shouldBe TextFieldState.Empty
                }
            }

            Then("description is empty") {
                scenario.flow.test {
                    awaitLastItem().description shouldBe TextFieldState.Empty
                }
            }
        }
    }

    Given("title updated") {

        When("title is empty") {
            val scenario = TestScenario(
                validateField = ReportBugAction.ValidateField(ReportBugField.Title, "")
            )

            Then("submit is disabled") {
                scenario.flow.test {
                    awaitLastItem().isSubmitEnabled shouldBe false
                }
            }

            Then("title is empty") {
                scenario.flow.test {
                    awaitLastItem().title.text shouldBe ""
                }
            }

            Then("title has error") {
                scenario.flow.test {
                    awaitLastItem().title.error.getOrNull() shouldBe TextRes(string.report_error_empty_title)
                }
            }
        }

        When("title is focused") {
            val scenario = TestScenario(
                state = ReportBugState.Empty.copy { ReportBugState.title.error set TextRes("error").some() },
                focusedField = ReportBugField.Title
            )

            Then("title has no error") {
                scenario.flow.test {
                    awaitLastItem().title.error shouldBe none()
                }
            }
        }
    }

    Given("description updated") {

        When("description is empty") {
            val scenario = TestScenario(
                validateField = ReportBugAction.ValidateField(ReportBugField.Description, "")
            )

            Then("submit is disabled") {
                scenario.flow.test {
                    awaitLastItem().isSubmitEnabled shouldBe false
                }
            }

            Then("description is empty") {
                scenario.flow.test {
                    awaitLastItem().description.text shouldBe ""
                }
            }

            Then("description has error") {
                scenario.flow.test {
                    awaitLastItem().description.error.getOrNull() shouldBe
                        TextRes(string.report_error_empty_description)
                }
            }
        }

        When("description is focused") {
            val scenario = TestScenario(
                state = ReportBugState.Empty.copy { ReportBugState.description.error set TextRes("error").some() },
                focusedField = ReportBugField.Description
            )

            Then("description has no error") {
                scenario.flow.test {
                    awaitLastItem().description.error shouldBe none()
                }
            }

            And("another field is empty") {

                Then("the other fields doesn't have error") {
                    scenario.flow.test {
                        with(awaitLastItem()) {
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
                validateField = ReportBugAction.ValidateField(ReportBugField.Steps, "")
            )

            Then("submit is disabled") {
                scenario.flow.test {
                    awaitLastItem().isSubmitEnabled shouldBe false
                }
            }

            Then("steps is empty") {
                scenario.flow.test {
                    awaitLastItem().steps.text shouldBe ""
                }
            }

            Then("steps has error") {
                scenario.flow.test {
                    awaitLastItem().steps.error.getOrNull() shouldBe TextRes(string.report_error_empty_steps)
                }
            }
        }

        When("steps is focused") {
            val scenario = TestScenario(
                state = ReportBugState.Empty.copy { ReportBugState.steps.error set TextRes("error").some() },
                focusedField = ReportBugField.Steps
            )

            Then("steps has no error") {
                scenario.flow.test {
                    awaitLastItem().steps.error shouldBe none()
                }
            }
        }
    }

    Given("expected behavior updated") {

        When("expected behavior is empty") {
            val scenario = TestScenario(
                validateField = ReportBugAction.ValidateField(ReportBugField.ExpectedBehavior, "")
            )

            Then("submit is disabled") {
                scenario.flow.test {
                    awaitLastItem().isSubmitEnabled shouldBe false
                }
            }

            Then("expected behavior is empty") {
                scenario.flow.test {
                    awaitLastItem().expectedBehavior.text shouldBe ""
                }
            }

            Then("expected behavior has error") {
                scenario.flow.test {
                    awaitLastItem().expectedBehavior.error.getOrNull() shouldBe
                        TextRes(string.report_error_empty_expected_behavior)
                }
            }
        }

        When("expected behavior is focused") {
            val scenario = TestScenario(
                state = ReportBugState.Empty.copy { ReportBugState.expectedBehavior.error set TextRes("error").some() },
                focusedField = ReportBugField.ExpectedBehavior
            )

            Then("expected behavior has no error") {
                scenario.flow.test {
                    awaitLastItem().expectedBehavior.error shouldBe none()
                }
            }
        }
    }
})

private class ForYouPresenterTestScenario(
    actionsFlow: Flow<ReportBugAction>,
    val sut: ReportBugPresenter
) {

    val flow = moleculeFlow(mode = RecompositionMode.Immediate) {
        sut.models(actions = actionsFlow)
    }.distinctUntilChanged()
}

private fun TestScenario(
    state: ReportBugState? = null,
    focusedField: ReportBugField? = null,
    validateField: ReportBugAction.ValidateField? = null,
    actions: Flow<ReportBugAction> = flow {
        state?.let { emit(ReportBugAction.ValidateAllFields) }
        focusedField?.let { emit(ReportBugAction.FocusChanged(it)) }
        validateField?.let { emit(it) }
    }
) = ForYouPresenterTestScenario(
    actionsFlow = actions,
    sut = ReportBugPresenter()
)
