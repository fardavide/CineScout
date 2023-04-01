package cinescout.suggestions.presentation

import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.suggestions.presentation.worker.UpdateSuggestionsWorker
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module

val SuggestionsPresentationModule = module {

    includes(SuggestionsPresentationAnnotationModule().module)

    single(named(ForYouViewModel.SuggestionsStackSizeName)) { 10 }
    factory { UpdateSuggestionsWorker.Scheduler(workManager = get()) }
}

@Module
@ComponentScan
class SuggestionsPresentationAnnotationModule
