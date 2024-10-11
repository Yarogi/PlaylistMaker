package com.example.playlistmaker.data.search.storage.impl

import com.example.playlistmaker.data.db.TrackDataBase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.mapper.TrackDbMapper
import com.example.playlistmaker.data.search.storage.HistoryStorage
import com.example.playlistmaker.domain.main.model.Track

class HistoryStorageImpl(
    private val dataBase: TrackDataBase,
    private val trackDbMapper: TrackDbMapper,
) :
    HistoryStorage {

    companion object {
        private val TRACK_LIMIT = 10
    }

    override fun save(list: List<Track>): Boolean {

        val removeTracksId = dataBase.trackDao()
            .getlAllHistoryId()
            .filter { trackId ->
                list.find { track -> track.trackId == trackId } == null
            }

        dataBase.trackDao().deleteHistory()
        removeTracksId.forEach { trackId ->
            dataBase.trackDao().deleteTrackByIdSafety(trackId = trackId)
        }

        list.forEach { track ->
            dataBase.trackDao().addToHistory(trackDbMapper.map(track))
        }

        return true
    }

    override fun read(): List<Track> {

        val tracks = dataBase.trackDao().getAllHistory().map {
            trackDbMapper.map(it)
        }

        return tracks

    }

    override fun add(track: Track) {

        val entity = trackDbMapper.map(track)
        dataBase.trackDao().addToHistory(entity)

        сutAccordingToLimit()
    }

    override fun clean() {
        dataBase.trackDao().clearHistorySafety()
    }

    private fun сutAccordingToLimit() {

        val activeHistory = dataBase.trackDao().getAllHistory()
        if (activeHistory.size > TRACK_LIMIT) {
            for (i in TRACK_LIMIT..activeHistory.size - 1) {

                val track: Track = trackDbMapper.map(activeHistory[i])
                val trackEntity: TrackEntity = trackDbMapper.map(track)

                dataBase.trackDao().removeFromHistory(trackEntity)
            }
        }

    }


}