package domain.use_cases

import domain.data.CompanyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import remote.RemoteFinanceInterface

class GetCompaniesForExploreUseCase : KoinComponent {

    private val remoteFinance: RemoteFinanceInterface by inject()
    private val exploreCompaniesTicker =
        mutableListOf("MSFT", "AAPL", "AMZN", "FB", "GOOGL", "IBM", "BRK.B")


    suspend fun invoke(): List<CompanyData> {
        val exploreList = mutableListOf<CompanyData>()
        for (ticker in exploreCompaniesTicker) {
            val data = remoteFinance.getCompanyData(ticker)
            if (data.name != null)
                exploreList.add(data)
        }
        return exploreList
    }
}