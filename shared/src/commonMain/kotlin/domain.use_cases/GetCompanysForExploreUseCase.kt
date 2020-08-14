package domain.use_cases

import domain.data.CompanyData
import kotlinx.coroutines.flow.collect
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface
import sql.ExploreDatabaseHelper

class GetCompanysForExploreUseCase : KoinComponent {

    private val remoteFinance: RemoteFinanceInterface by inject()
    private val exploreDatabaseHelper: ExploreDatabaseHelper by inject()

    private val exploreCompaniesTicker =
        mutableListOf("MSFT", "AAPL", "AMZN", "FB", "GOOGL", "IBM", "BRK.B")


    suspend fun invoke(): List<CompanyData> {
        val data = exploreDatabaseHelper.selectAllItems().collect {  }
        val exploreList = mutableListOf<CompanyData>()
        for (ticker in exploreCompaniesTicker) {
            val data = remoteFinance.getCompanyData(ticker)
            if (data.name != null)
                exploreList.add(data)
        }
        return exploreList
    }
}