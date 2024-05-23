<script setup>

	import {ref, reactive} from 'vue'
	import {useRoute, useRouter} from "vue-router"
	import {RefundData, RefundApply} from '^/order.js'
	import {showNotify} from "vant"

	const route = useRoute()
	const router = useRouter()

	const data = reactive({

		showReasonPicker: false,
		showMethodPicker: false,
		reasons: [],
		methods: [],

		order: {},
		form: {
			orderId: null,
			reason: '',
			refundAmount: '',
			method: '',
			description: '',
			images: [],
		}
	})

	const onReasonConfirm = ({ selectedValues}) => {
		data.showReasonPicker = false
		data.form.reason = selectedValues[0]
	}
	const onMethodConfirm = ({ selectedValues}) => {
		data.showMethodPicker = false
		data.form.method = selectedValues[0]
	}

	const orderId = route.query.id

	const initData = () => {
		RefundData(orderId).then(res => {
			data.reasons = res.data.reasons.map(e => {
				return {
					text: e,
					value: e
				}
			})
			data.methods = res.data.methods.map(e => {
				return {
					text: e,
					value: e
				}
			})
			data.order = res.data.order

			data.form.refundAmount = data.order.totalAmount
			data.form.orderId = orderId
		})
	}
	initData()

	const submitHandle = () => {
		let formData = new FormData()
		for (let key in data.form) {
			formData.append(key, data.form[key])
		}
		if (data.form.images.length > 0) {
			for (let index = 0; index < data.form.images.length; index++) {
				const pic = data.form.images[index]
				formData.append("pics", pic.file)
			}
		}
		RefundApply(formData).then(res => {
			showNotify({type: 'primary', message: '操作成功'})
			router.back()
		})
	}

</script>

<template>
	<van-nav-bar title="申请退款" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<van-card
			v-for="item in data.order.orderItems"
			:title="item.productName"
			:price="item.price"
			:num="item.quantity"
			:thumb="item.productImage"
	>
		<template #tags>
			<van-tag color="#c8c9cc" text-color="white" style="margin: 6px 6px 0 0;"
			         v-for="it in item.sku.split(';')">
				{{it}}
			</van-tag>
		</template>
	</van-card>

	<van-form @submit="submitHandle" style="margin-top: 20px;">
		<van-cell-group inset>
			<van-field
					v-model="data.form.reason"
					is-link
					readonly
					label="* 退款原因"
					placeholder="请选择"
					@click="data.showReasonPicker = true"
					input-align="right"
					:rules="[{ required: true, message: '请选择退款原因' }]"
			/>
			<van-field
					v-model="data.form.refundAmount"
					label="退款金额"
					placeholder="退款金额"
					:rules="[{ required: true, message: '请填写退款金额' }]"
					input-align="right"
					disabled
			/>
			<van-field
					v-model="data.form.method"

					is-link
					readonly
					label="* 退货方式"
					placeholder="请选择"
					@click="data.showMethodPicker = true"
					input-align="right"
					:rules="[{ required: true, message: '请选择退货方式' }]"
			/>

			<van-field
					v-model="data.form.description"

					rows="3"
					autosize
					label="补充描述"
					type="textarea"
					maxlength="200"
					placeholder="请输入补充描述"
					show-word-limit
					input-align="right"
			/>

			<van-field name="uploader" label="凭证上传">
				<template #input>
					<van-uploader v-model="data.form.images"/>
				</template>
			</van-field>

			<van-popup v-model:show="data.showReasonPicker" position="bottom">
				<van-picker
						:columns="data.reasons"
						@confirm="onReasonConfirm"
						@cancel="data.showReasonPicker = false"
				/>
			</van-popup>
			<van-popup v-model:show="data.showMethodPicker" position="bottom">
				<van-picker
						:columns="data.methods"
						@confirm="onMethodConfirm"
						@cancel="data.showMethodPicker = false"
				/>
			</van-popup>

		</van-cell-group>
		<div style="margin: 16px;">
			<van-button round block type="primary" native-type="submit">提交</van-button>
		</div>
	</van-form>

</template>