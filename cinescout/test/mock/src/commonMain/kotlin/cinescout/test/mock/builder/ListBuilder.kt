package cinescout.test.mock.builder

import cinescout.screenplay.domain.model.Screenplay

@MockAppBuilderDsl
class ListBuilder internal constructor() {

    internal var list: List<Screenplay> = emptyList()
        private set

    fun add(screenplay: Screenplay) {
        list = list + screenplay
    }
}
