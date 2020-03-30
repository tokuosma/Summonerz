package com.example.summonerz.barcodedetection

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.summonerz.CreateMonster
import com.example.summonerz.Monster
import com.example.summonerz.MonsterViewModel
import com.example.summonerz.R
import com.example.summonerz.camera.WorkflowModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MonsterScannedResultFragment: BottomSheetDialogFragment() {

    private lateinit var monster:Monster
    private val monsterViewModel: MonsterViewModel by activityViewModels()
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val view = layoutInflater.inflate(R.layout.monster_scanned_bottom_sheet, viewGroup)

        val arguments = arguments
        val rawValueString:String =
            if (arguments?.containsKey(ARG_RAW_VALUE_STRING) == true) {
                arguments.getString(ARG_RAW_VALUE_STRING) ?: String()
            } else {
                Log.e(TAG, "No barcode field list passed in!")
                ""
            }
        val rawValueBytes=
            if (arguments?.containsKey(ARG_RAW_VALUE_BYTES) == true) {
                arguments.getByteArray(ARG_RAW_VALUE_BYTES) ?: ByteArray(0)
            } else {
                Log.e(TAG, "No barcode field list passed in!")
                ByteArray(0)
            }

        monster = CreateMonster.createMonster(context,rawValueString, rawValueBytes)
//        view.findViewById<RecyclerView>(R.id.barcode_field_recycler_view).apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(activity)
//            adapter = BarcodeFieldAdapter(barcodeFieldList)
//        }

        view.findViewById<TextView>(R.id.monster_name).text = monster.name
        view.findViewById<ImageView>(R.id.monster_icon).setImageResource(resources.getIdentifier(monster.icon , "drawable",
            activity?.packageName
        )
        )

//        view.findViewById<Button>(R.id.button_save).setOnClickListener {
//            GlobalScope.launch {
//                val db = Room.databaseBuilder(requireActivity().applicationContext, MonsterDatabase::class.java, "monsters")
//                    .build()
//                db.monsterDao().insert(monster = monster)
//                db.close()
//                requireActivity().finish()
//            }
//        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.button_save).setOnClickListener {
            monsterViewModel.insert(this.monster)
            requireActivity().finish()
        }
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        activity?.let {
            // Back to working state after the bottom sheet is dismissed.
            val model: WorkflowModel by activityViewModels()
            model.setWorkflowState(WorkflowModel.WorkflowState.DETECTING)
        }
        super.onDismiss(dialogInterface)
    }

    companion object {

        private const val TAG = "MonsterScannedResult"
        private const val ARG_RAW_VALUE_STRING = "arg_barcode_raw_value"
        private const val ARG_RAW_VALUE_BYTES = "arg_raw_value_bytes"


        fun show(
            fragmentManager: FragmentManager,
            rawValueString: String?,
            rawValueBytes: ByteArray? ) {
            val monsterScannedResultFragment = MonsterScannedResultFragment()
            monsterScannedResultFragment.arguments = Bundle().apply {
                putByteArray(ARG_RAW_VALUE_BYTES,rawValueBytes)
                putString(ARG_RAW_VALUE_STRING, rawValueString)
            }
            monsterScannedResultFragment.show(fragmentManager, TAG)
        }

        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as MonsterScannedResultFragment?)?.dismiss()
        }
    }
}
