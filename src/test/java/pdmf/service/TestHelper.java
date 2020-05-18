package pdmf.service;

import java.util.UUID;

import pdmf.model.OperationKey;
import pdmf.model.OperationRec;
import pdmf.model.ProcessKey;
import pdmf.model.ProcessRec;
import pdmf.model.ProductKey;
import pdmf.model.ProductRec;
import pdmf.model.TopicKey;
import pdmf.model.TopicRec;

public class TestHelper {

	private static Integer version = 1;

	private static ProductService productService = new ProductService();
	private static TopicService topicService = new TopicService();
	private static ProcessService processService = new ProcessService();
	private static OperationService operationService = new OperationService();

	protected static void setupDataForProductTests() {

		String productName = "ois";
		String productDescription = "Order Invoicing Sales";
		ProductKey productKey = new ProductKey(version, productName);
		ProductRec productRec = new ProductRec(productKey, productDescription, null, null);
		productRec.description = "ois longdescr";
		productRec.shortdescr = "ois shortdescr";
		productRec.status = "wrk";
		productService.store(productRec, "test");

		productName = "uvms";
		productDescription = "Båtar och fiske";
		productKey = new ProductKey(version, productName);
		productRec = new ProductRec(productKey, productDescription, null, null);
		productRec.description = "uvms longdescr";
		productRec.shortdescr = "uvms shortdescr";
		productRec.status = "wrk";
		productService.store(productRec, "test");

	}

	protected static void setupOperationTestData1() {

		String productName = "ois";
		String productDescription = "Order Invoicing Sales";

		String customer = "customer";
		String customerDescription = "Maintain customer";

		String item = "item";
		String itemDescription = "Maintain item";

		String customerorder = "customerorder";
		String customerorderDescription = "Maintain customer orders";

		String invoicing = "invoicing";
		String invoiceDescription = "Invoice customerorders that are delivered";

		ProductKey productKey = new ProductKey(version, productName);
		ProductRec productRec = new ProductRec(productKey, productDescription, null, null);
		productRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
		productRec.status = "wrk";
		productService.store(productRec, "test");

		{
			TopicKey topicKey = new TopicKey(version, productName, customer);
			TopicRec topicRec = new TopicRec(topicKey, customerDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

		{
			TopicKey topicKey = new TopicKey(version, productName, item);
			TopicRec topicRec = new TopicRec(topicKey, itemDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

		{
			TopicKey topicKey = new TopicKey(version, productName, customerorder);
			TopicRec topicRec = new TopicRec(topicKey, customerorderDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}
		{
			TopicKey topicKey = new TopicKey(version, productName, invoicing);
			TopicRec topicRec = new TopicRec(topicKey, invoiceDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

		{
			ProcessKey processKey = new ProcessKey(version, productName, customer, "crud", 0);
			ProcessRec processRec = new ProcessRec(processKey, "maintain customer", null, null);
			processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			processService.store(processRec, "test");

			{
				OperationKey key = new OperationKey(version, productName, customer, "crud", 0, "add", 0);
				OperationRec op = new OperationRec(key, "add customer", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{

				OperationKey key = new OperationKey(version, productName, customer, "crud", 0, "change", 0);
				OperationRec op = new OperationRec(key, "change customerinfo", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{
				OperationKey key = new OperationKey(version, productName, customer, "crud", 0, "delete", 0);
				OperationRec op = new OperationRec(key, "remove a customer", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}

		}

		{
			ProcessKey processKey = new ProcessKey(version, productName, item, "crud", 0);
			ProcessRec processRec = new ProcessRec(processKey, "maintain item", null, null);
			processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			processService.store(processRec, "test");

			{
				OperationKey key = new OperationKey(version, productName, item, "crud", 0, "add", 0);
				OperationRec op = new OperationRec(key, "add item", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{

				OperationKey key = new OperationKey(version, productName, item, "crud", 0, "change", 0);
				OperationRec op = new OperationRec(key, "change item", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{
				OperationKey key = new OperationKey(version, productName, item, "crud", 0, "delete", 0);
				OperationRec op = new OperationRec(key, "remove an item", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}

		}

		{
			ProcessKey processKey = new ProcessKey(version, productName, customerorder, "crud", 0);
			ProcessRec processRec = new ProcessRec(processKey, "maintain customer orders", null, null);
			processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			processService.store(processRec, "test");

			{
				OperationKey key = new OperationKey(version, productName, customerorder, "crud", 0, "add", 0);
				OperationRec op = new OperationRec(key, "add customerOrder", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{

				OperationKey key = new OperationKey(version, productName, customerorder, "crud", 0, "change", 0);
				OperationRec op = new OperationRec(key, "change customerOrder", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{
				OperationKey key = new OperationKey(version, productName, customerorder, "crud", 0, "delete", 0);
				OperationRec op = new OperationRec(key, "remove an customerOrder", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}

		}

		{
			ProcessKey processKey = new ProcessKey(version, productName, invoicing, "invoice", 0);
			ProcessRec processRec = new ProcessRec(processKey, "invoice orders", null, null);
			processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			processService.store(processRec, "test");

			{
				OperationKey key = new OperationKey(version, productName, invoicing, "invoice", 0, "crt", 0);
				OperationRec op = new OperationRec(key, "create invoice", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{

				OperationKey key = new OperationKey(version, productName, invoicing, "invoice", 0, "snd", 1);
				OperationRec op = new OperationRec(key, "send invoice", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{
				OperationKey key = new OperationKey(version, productName, invoicing, "invoice", 0, "upd", 2);
				OperationRec op = new OperationRec(key, "update stock balance", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}
			{
				OperationKey key = new OperationKey(version, productName, invoicing, "invoice", 0, "upd", 3);
				OperationRec op = new OperationRec(key, "update customerbalance", null, null);
				op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				operationService.store(op, "test");
			}

		}

	}

	protected static void setupOperationTestData2() {

		String productName = "uvms";
		String productDescription = "a VMS system for marine traffic";

		ProductKey productKey = new ProductKey(version, productName);
		ProductRec productRec = new ProductRec(productKey, productDescription, null, null);
		productRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
		productRec.status = "wrk";
		productService.store(productRec, "test");

		{
			String topic = "assets";
			String topicDescription = "fiskebåtar av olika slag";

			TopicKey topicKey = new TopicKey(version, productName, topic);
			TopicRec topicRec = new TopicRec(topicKey, topicDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");

			{
				ProcessKey processKey = new ProcessKey(version, productName, topic, "crud", 11);
				ProcessRec processRec = new ProcessRec(processKey, "maintain assets", null, null);
				processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				processService.store(processRec, "test");

				{
					OperationKey key = new OperationKey(version, productName, topic, "crud", 11, "upd", 3);
					OperationRec op = new OperationRec(key, "register a vessel manually", null, null);
					op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
					operationService.store(op, "test");
				}

				{
					OperationKey key = new OperationKey(version, productName, topic, "crud", 11, "dlt", 114);
					OperationRec op = new OperationRec(key, "remove a vessel", null, null);
					op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
					operationService.store(op, "test");
				}
				{
					OperationKey key = new OperationKey(version, productName, topic, "crud", 11, "upd", 112);
					OperationRec op = new OperationRec(key, "update a vcessels information", null, null);
					op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
					operationService.store(op, "test");
				}

				{
					OperationKey key = new OperationKey(version, productName, topic, "crud", 11, "imp", 125);
					OperationRec op = new OperationRec(key, "import from external provider", null, null);
					op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
					operationService.store(op, "test");
				}
			}
		}

		{

			String topic = "MT";
			String topicDescription = "MobileTerminal";

			TopicKey topicKey = new TopicKey(version, productName, topic);
			TopicRec topicRec = new TopicRec(topicKey, topicDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");

			{

				ProcessKey processKey = new ProcessKey(version, productName, topic, "crud", 21);
				ProcessRec processRec = new ProcessRec(processKey, "maintain mobileterminals", null, null);
				processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				processService.store(processRec, "test");

				{
					OperationKey key = new OperationKey(version, productName, topic, "crud", 21, "crt", 221);
					OperationRec op = new OperationRec(key, "register a mobileterminal", null, null);
					op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
					operationService.store(op, "test");
				}
				{
					OperationKey key = new OperationKey(version, productName, topic, "crud", 21, "dlt", 224);
					OperationRec op = new OperationRec(key, "remove a mobileterminal", null, null);
					op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
					operationService.store(op, "test");
				}
				{
					OperationKey key = new OperationKey(version, productName, topic, "crud", 21, "upd", 222);
					OperationRec op = new OperationRec(key, "update a mobileterminal information", null, null);
					op.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
					operationService.store(op, "test");
				}
			}
		}

	}

	//// PROCESS

	protected static void setupProcessTestData1() {

		String productName = "ois";
		String productDescription = "Order Invoicing Sales";

		String customer = "customer";
		String customerDescription = "Maintain customer";

		String item = "item";
		String itemDescription = "Maintain item";

		String customerorder = "customerorder";
		String customerorderDescription = "Maintain customer orders";

		String invoicing = "invoicing";
		String invoiceDescription = "Invoice customerorders that are delivered";

		ProductKey productKey = new ProductKey(version, productName);
		ProductRec productRec = new ProductRec(productKey, productDescription, null, null);
		productRec.shortdescr = "shortdescr";
		productRec.status = "wrk";
		productService.store(productRec, "test");

		{
			TopicKey topicKey = new TopicKey(version, productName, customer);
			TopicRec topicRec = new TopicRec(topicKey, customerDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

		{
			TopicKey topicKey = new TopicKey(version, productName, item);
			TopicRec topicRec = new TopicRec(topicKey, itemDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

		{
			TopicKey topicKey = new TopicKey(version, productName, customerorder);
			TopicRec topicRec = new TopicRec(topicKey, customerorderDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}
		{
			TopicKey topicKey = new TopicKey(version, productName, invoicing);
			TopicRec topicRec = new TopicRec(topicKey, invoiceDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

		{
			ProcessKey processKey = new ProcessKey(version, productName, customer, "crud", 0);
			ProcessRec processRec = new ProcessRec(processKey, "maintain customer", null, null);
			processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			processService.store(processRec, "test");

		}

		{
			ProcessKey processKey = new ProcessKey(version, productName, item, "crud", 0);
			ProcessRec processRec = new ProcessRec(processKey, "maintain item", null, null);
			processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			processService.store(processRec, "test");

		}

		{
			ProcessKey processKey = new ProcessKey(version, productName, customerorder, "crud", 0);
			ProcessRec processRec = new ProcessRec(processKey, "maintain customer orders", null, null);
			processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			processService.store(processRec, "test");

		}

		{
			ProcessKey processKey = new ProcessKey(version, productName, invoicing, "invoice", 0);
			ProcessRec processRec = new ProcessRec(processKey, "invoice orders", null, null);
			processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			processService.store(processRec, "test");

		}

	}

	protected static void setupProcessTestData2() {

		String productName = "uvms";
		String productDescription = "a VMS system for marine traffic";

		ProductKey productKey = new ProductKey(version, productName);
		ProductRec productRec = new ProductRec(productKey, productDescription, null, null);
		productRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
		productRec.status = "wrk";
		productService.store(productRec, "test");

		{
			String topic = "assets";
			String topicDescription = "fiskebåtar av olika slag";

			TopicKey topicKey = new TopicKey(version, productName, topic);
			TopicRec topicRec = new TopicRec(topicKey, topicDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");

			{
				ProcessKey processKey = new ProcessKey(version, productName, topic, "crud", 11);
				ProcessRec processRec = new ProcessRec(processKey, "maintain assets", null, null);
				processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				processService.store(processRec, "test");

			}
		}

		{

			String topic = "MT";
			String topicDescription = "MobileTerminal";

			TopicKey topicKey = new TopicKey(version, productName, topic);
			TopicRec topicRec = new TopicRec(topicKey, topicDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");

			{

				ProcessKey processKey = new ProcessKey(version, productName, topic, "crud", 21);
				ProcessRec processRec = new ProcessRec(processKey, "maintain mobileterminals", null, null);
				processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				processService.store(processRec, "test");

			}
		}

	}

	// TOPIC

	//// PROCESS

	protected static void setupTopicTestData1() {

		String productName = "ois";
		String productDescription = "Order Invoicing Sales";

		String customer = "customer";
		String customerDescription = "Maintain customer";

		String item = "item";
		String itemDescription = "Maintain item";

		String customerorder = "customerorder";
		String customerorderDescription = "Maintain customer orders";

		String invoicing = "invoicing";
		String invoiceDescription = "Invoice customerorders that are delivered";

		ProductKey productKey = new ProductKey(version, productName);
		ProductRec productRec = new ProductRec(productKey, productDescription, null, null);
		productRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
		productRec.status = "wrk";
		productService.store(productRec, "test");

		{
			TopicKey topicKey = new TopicKey(version, productName, customer);
			TopicRec topicRec = new TopicRec(topicKey, customerDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

		{
			TopicKey topicKey = new TopicKey(version, productName, item);
			TopicRec topicRec = new TopicRec(topicKey, itemDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

		{
			TopicKey topicKey = new TopicKey(version, productName, customerorder);
			TopicRec topicRec = new TopicRec(topicKey, customerorderDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}
		{
			TopicKey topicKey = new TopicKey(version, productName, invoicing);
			TopicRec topicRec = new TopicRec(topicKey, invoiceDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}
	}

	protected static void setupTopicTestData2() {

		String productName = "uvms";
		String productDescription = "a VMS system for marine traffic";

		ProductKey productKey = new ProductKey(version, productName);
		ProductRec productRec = new ProductRec(productKey, productDescription, null, null);
		productRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
		productRec.status = "wrk";
		productService.store(productRec, "test");

		{
			String topic = "assets";
			String topicDescription = "fiskebåtar av olika slag";

			TopicKey topicKey = new TopicKey(version, productName, topic);
			TopicRec topicRec = new TopicRec(topicKey, topicDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");

			{
				ProcessKey processKey = new ProcessKey(version, productName, topic, "crud", 11);
				ProcessRec processRec = new ProcessRec(processKey, "maintain assets", null, null);
				processRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
				processService.store(processRec, "test");
			}
		}

		{

			String topic = "MT";
			String topicDescription = "MobileTerminal";

			TopicKey topicKey = new TopicKey(version, productName, topic);
			TopicRec topicRec = new TopicRec(topicKey, topicDescription, null, null);
			topicRec.shortdescr = "shortdescr_" + UUID.randomUUID().toString();
			topicService.store(topicRec, "test");
		}

	}

}
