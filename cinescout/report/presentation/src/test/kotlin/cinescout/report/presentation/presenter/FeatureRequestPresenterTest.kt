package cinescout.report.presentation.presenter

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import arrow.core.none
import cinescout.report.domain.usecase.FakeBuildReportLinks
import cinescout.report.presentation.action.FeatureRequestAction
import cinescout.report.presentation.model.FeatureRequestField
import cinescout.report.presentation.model.TextFieldState
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.test.android.MoleculeTestExtension
import cinescout.test.kotlin.awaitLastItem
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class FeatureRequestPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension())

    Given("presenter") {

        When("started") {
            val scenario = TestScenario()

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

        When("submit") {
            val scenario = TestScenario(
                actions = flow {
                    val submit = FeatureRequestAction.Submit(
                        alternativeSolutions = "",
                        description = "",
                        title = ""
                    )
                    emit(submit)
                }
            )

            Then("all fields are validated") {
                scenario.flow.test {
                    with(awaitLastItem()) {
                        title.error.getOrNull() shouldBe TextRes(string.report_error_empty_title)
                        description.error.getOrNull() shouldBe TextRes(string.report_error_empty_description)
                        alternativeSolutions.error.getOrNull() shouldBe
                            TextRes(string.report_error_empty_alternative_solutions)
                    }
                }
            }
        }
    }

    Given("title updated") {

        When("title is empty") {
            val scenario = TestScenario(
                validateField = FeatureRequestAction.ValidateField(FeatureRequestField.Title, "")
            )

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
                focusedField = FeatureRequestField.Title
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
                validateField = FeatureRequestAction.ValidateField(FeatureRequestField.Description, "")
            )

            Then("description has error") {
                scenario.flow.test {
                    awaitLastItem().description.error.getOrNull() shouldBe
                        TextRes(string.report_error_empty_description)
                }
            }
        }

        When("description is focused") {
            val scenario = TestScenario(
                focusedField = FeatureRequestField.Description
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
                            alternativeSolutions.error shouldBe none()
                        }
                    }
                }
            }
        }
    }

    Given("alternative solutions updated") {

        When("alternative solutions is empty") {
            val scenario = TestScenario(
                validateField = FeatureRequestAction.ValidateField(FeatureRequestField.AlternativeSolutions, "")
            )

            Then("alternative solutions has error") {
                scenario.flow.test {
                    awaitLastItem().alternativeSolutions.error.getOrNull() shouldBe
                        TextRes(string.report_error_empty_alternative_solutions)
                }
            }
        }

        When("alternative solutions is focused") {
            val scenario = TestScenario(
                focusedField = FeatureRequestField.AlternativeSolutions
            )

            Then("alternative solutions has no error") {
                scenario.flow.test {
                    awaitLastItem().alternativeSolutions.error shouldBe none()
                }
            }
        }
    }
})

private class FeatureRequestTestScenario(
    actionsFlow: Flow<FeatureRequestAction>,
    val sut: FeatureRequestPresenter
) {

    val flow = moleculeFlow(mode = RecompositionMode.Immediate) {
        sut.models(actions = actionsFlow)
    }.distinctUntilChanged()
}

private fun TestScenario(
    focusedField: FeatureRequestField? = null,
    validateField: FeatureRequestAction.ValidateField? = null,
    actions: Flow<FeatureRequestAction> = flow {
        focusedField?.let { emit(FeatureRequestAction.FocusChanged(it)) }
        validateField?.let { emit(it) }
    }
) = FeatureRequestTestScenario(
    actionsFlow = actions,
    sut = FeatureRequestPresenter(FakeBuildReportLinks())
)
