package cinescout.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class CineScoutRuleSetProvider : RuleSetProvider {

    override val ruleSetId: String = "CineScout"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                UseComposableActions()
            )
        )
    }
}
